package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.*;
import com.shyu.NeoNest.dto.request.AdminOrderFilterDto;
import com.shyu.NeoNest.dto.request.OrderCreateDto;
import com.shyu.NeoNest.dto.request.OrderItemDto;
import com.shyu.NeoNest.dto.response.*;
import com.shyu.NeoNest.enums.DeliveryStatus;
import com.shyu.NeoNest.enums.OrderStatus;
import com.shyu.NeoNest.enums.PaymentStatus;
import com.shyu.NeoNest.exception.MemberNotFoundException;
import com.shyu.NeoNest.exception.OrderNotFoundException;
import com.shyu.NeoNest.repository.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartRepository cartRepository;
    private final DeliveryRepository deliveryRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public String createOrder(OrderCreateDto dto) {

        Member getMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. 멤버 ID : " + dto.getMemberId()));

        List<OrderItemDto> orderItemsDto = dto.getOrderItems();
        Map<Long, Product> productMap = new HashMap<>();
        String orderName = "";
        BigDecimal totalPrice = BigDecimal.ZERO;

        // 1. 상품 검증 및 주문 이름 생성 로직
        for (int i = 0; i < orderItemsDto.size(); i++) {
            OrderItemDto orderItemDto = orderItemsDto.get(i);

            // 1-1. 상품 검증 및 캐싱
            Product getProduct = productMap.computeIfAbsent(orderItemDto.getProductId(),
                    productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")));

            // 1-2. 주문 이름 생성 (한 건일 경우 상품명 그대로, 여러 건일 경우 첫 번째 상품명 + "외 n건")
            if (i == 0) {
                orderName = getProduct.getName();
            } else if (i == 1) {
                orderName += " 외 " + (orderItemsDto.size() - 1) + "건";
            }

            // 1-3. 가격 계산
            BigDecimal itemTotalPrice = BigDecimal.valueOf(getProduct.getOriginalPrice() * orderItemDto.getQuantity());
            totalPrice = totalPrice.add(itemTotalPrice);
        }

        // 2. 결제 정보 생성 및 저장
        Payment newPayment = createAndSavePayment(totalPrice);

        // 3. 새로운 주문 생성
        Order newOrder = createAndSaveOrder(getMember, newPayment, orderName);

        // 4. 주문 상품 저장, 카트 아이템 삭제
        saveOrderItemsAndRemoveFromCart(orderItemsDto, productMap, newOrder, getMember);

        return newOrder.getOrderUid();
    }

    private Payment createAndSavePayment(BigDecimal totalPrice) {
        Payment newPayment = Payment.builder()
                .price(totalPrice)
                .status(PaymentStatus.READY)
                .build();
        paymentRepository.save(newPayment);
        return newPayment;
    }

    private Order createAndSaveOrder(Member getMember, Payment newPayment, String orderName) {
        Order newOrder = Order.builder()
                .member(getMember)
                .payment(newPayment)
                .orderName(orderName) // 생성된 주문 이름을 설정
                .orderDate(LocalDateTime.now())
                .orderUid(UUID.randomUUID().toString())
                .status(OrderStatus.READY)
                .build();
        orderRepository.save(newOrder);
        return newOrder;
    }

    private void saveOrderItemsAndRemoveFromCart(List<OrderItemDto> orderItemsDto, Map<Long, Product> productMap, Order newOrder, Member getMember) {
        for (OrderItemDto orderItemDto : orderItemsDto) {
            Product getProduct = productMap.get(orderItemDto.getProductId()); // 캐싱된 상품 가져오기

            // 4-1. 주문 상품 생성 및 저장
            OrderProduct newOrderProduct = OrderProduct.builder()
                    .order(newOrder)
                    .product(getProduct)
                    .price(getProduct.getOriginalPrice())
                    .quantity(orderItemDto.getQuantity())
                    .build();
            newOrder.addOrderProduct(newOrderProduct);
            orderProductRepository.save(newOrderProduct);

            // 4-2. 장바구니에서 제거
            if (orderItemDto.getCartId() != null) {
                cartRepository.deleteCartById(getMember.getMemberId(), orderItemDto.getCartId());
            }
        }
    }


    // orderUid로 주문 페이지 진입 시 오더 조회
    @Transactional(readOnly = true)
    public OrderReadyPageDto getOrder(String orderUid) {
        if (orderUid == null || orderUid.isEmpty()) {
            throw new IllegalArgumentException("유효한 orderUid를 입력해주세요.");
        }

        return orderRepository.getOrderDto(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("해당 오더 Uid에 대한 데이터를 찾을 수 없습니다."));
    }

    // 마이 페이지 나의 주문 조회
    @Transactional(readOnly = true)
    public OrderListGetDto getOrderListPageDto(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 멤버 ID를 입력해주세요.");
        }

        return orderRepository.findAllOrdersByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 데이터를 찾을 수 없습니다."));
    }

    // 마이 페이지 나의 주문 상세 조회
    @Transactional(readOnly = true)
    public List<MyPageOrderDto> getMyOrderPageDto(Long memberId, String orderUid) {
        // 입력 값 검증
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효한 멤버 ID를 입력해주세요.");
        }

        if (orderUid == null || orderUid.trim().isEmpty()) {
            throw new IllegalArgumentException("유효한 오더 Uid를 입력해주세요.");
        }

        // 나의 주문 상세 조회
        List<MyPageOrderDto> myPageOrderDtoByOrderUid = orderRepository.getMyPageOrderDtoByOrderUid(memberId, orderUid);

        if (myPageOrderDtoByOrderUid == null) {
            throw new IllegalArgumentException("잘못된 오더 Uid 입니다.");
        }

        return myPageOrderDtoByOrderUid;
    }

    // 어드민 페이지 오더 조회
    @Transactional
    public List<AdminOrderListDto> findAdminOrderListDto(AdminOrderFilterDto filterDto) {
        // 입력 값 검증
        if (filterDto == null) {
            throw new IllegalArgumentException("필터 정보가 필요합니다.");
        }

        if (filterDto.getStartDate() != null && filterDto.getEndDate() != null
                && filterDto.getStartDate().isAfter(filterDto.getEndDate())) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
        }

        // 필터 조건에 맞는 주문 리스트 조회
        List<AdminOrderListDto> adminOrderList = orderRepository.findAdminOrderListDtoByFilter(filterDto);

        return adminOrderList == null ? Collections.emptyList() : adminOrderList;
    }

    // 어드민 페이지 오더 상태 수정
    @Transactional
    public void changeOrderStatus(Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("잘못된 요청입니다."));

        Delivery delivery = order.getDelivery();

        if (orderStatus.equals("CANCEL") && order.getStatus().equals(OrderStatus.READY)) {
            order.changeStatus(OrderStatus.CANCEL);
        } else if (orderStatus.equals("COMPLETE") && order.getStatus().equals(OrderStatus.ING)) {
            order.changeStatus(OrderStatus.COMPLETE);

            if (delivery.getStatus().equals(DeliveryStatus.READY)) {
                delivery.changeDeliveryStatus(DeliveryStatus.COMPLETE);
            }
        }

        orderRepository.save(order);
        deliveryRepository.save(delivery);
    }

    @Transactional(readOnly = true)
    public AdminOrderDetailDto findAdminOrderDetailDtoList(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("유효한 오더 Id를 입력해주세요.");
        }

        return orderRepository.findAdminOrderDetailDtoList(orderId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요청입니다."));
    }


}
