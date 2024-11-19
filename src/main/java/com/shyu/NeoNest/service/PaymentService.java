package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Delivery;
import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.dto.request.PaymentCallbackRequest;
import com.shyu.NeoNest.dto.response.SuccessPageDto;
import com.shyu.NeoNest.dto.response.OrderPaymentDto;
import com.shyu.NeoNest.enums.DeliveryStatus;
import com.shyu.NeoNest.enums.OrderStatus;
import com.shyu.NeoNest.enums.PaymentStatus;
import com.shyu.NeoNest.repository.DeliveryRepository;
import com.shyu.NeoNest.repository.OrderRepository;
import com.shyu.NeoNest.repository.PaymentRepository;
import com.shyu.NeoNest.repository.ProductRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;
    private final IamportClient iamportClient;

    @Transactional
    public OrderPaymentDto getOrderPaymentInfo(String orderUid) {
        if (orderUid == null || orderUid.trim().isEmpty()) {
            throw new IllegalArgumentException("주문 Uid는 필수 값입니다.");
        }

        Order findOrder = orderRepository.findOrderAndPaymentAndMember(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 주문 Uid입니다."));

        return OrderPaymentDto.builder()
                .orderUid(findOrder.getOrderUid())
                .orderName(findOrder.getOrderName())
                .paymentPrice(findOrder.getPayment().getPrice())
                .buyerEmail(findOrder.getMember().getEmail())
                .buyerName(findOrder.getMember().getName())
                .buyerPhone(findOrder.getMember().getPhoneNumber())
                .buyerAddress(findOrder.getMember().getAddress())
                .buyPostcode(findOrder.getMember().getPostcode())
                .build();
    }

    @Transactional
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {

        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Order order = orderRepository.findOrderAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if(!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            BigDecimal price = order.getPayment().getPrice();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if(price.compareTo(BigDecimal.valueOf(iamportPrice)) != 0) {
                // 주문, 결제 삭제
                orderRepository.delete(order);
                paymentRepository.delete(order.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경, 방법 추가
            com.shyu.NeoNest.domain.Payment payment = order.getPayment();
            payment.changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());
            payment.changePayMethod(request.getPayMethod());
            payment.setPaymentSuccessTime(LocalDateTime.now());
            paymentRepository.save(payment);

            request.getPaymentItems().forEach(paymentItemDto -> {
                Product getProduct = productRepository.findById(paymentItemDto.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

                getProduct.removeStock(paymentItemDto.getQuantity());
                productRepository.save(getProduct);
            });

            Delivery newDelivery = Delivery.builder()
                    .order(order)
                    .recipientName(request.getRecipientName())
                    .phoneNumber(request.getPhoneNumber())
                    .postcode(request.getPostcode())
                    .address(request.getAddress())
                    .deliveryRequest(request.getDeliveryRequest())
                    .status(DeliveryStatus.READY)
                    .build();
            deliveryRepository.save(newDelivery);

            order.changeStatus(OrderStatus.ING);
            order.addDelivery(newDelivery);
            orderRepository.save(order);

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public SuccessPageDto getDeliveryInfo(String orderUid) {
        return orderRepository.getDeliveryDto(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 정보를 찾을 수 없습니다."));
    }

}
