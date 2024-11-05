package com.shyu.NeoNest.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.dto.request.AdminOrderFilterDto;
import com.shyu.NeoNest.dto.response.*;
import com.shyu.NeoNest.enums.DeliveryStatus;
import com.shyu.NeoNest.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shyu.NeoNest.domain.QDelivery.delivery;
import static com.shyu.NeoNest.domain.QMember.member;
import static com.shyu.NeoNest.domain.QOrder.order;
import static com.shyu.NeoNest.domain.QOrderProduct.orderProduct;
import static com.shyu.NeoNest.domain.QPayment.payment;
import static com.shyu.NeoNest.domain.QProduct.product;
import static com.shyu.NeoNest.domain.QReview.review;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Order> findOrderByUid(String Uid) {
        Order getOrder = queryFactory
                .selectFrom(order)
                .where(order.orderUid.eq(Uid))
                .fetchOne();

        return Optional.ofNullable(getOrder);
    }

    @Override
    public Optional<OrderReadyPageDto> getOrderDto(String orderUid) {
        List<Tuple> result = queryFactory
                .select(
                        order.orderId,
                        order.member.memberId,
                        order.orderDate,
                        order.status,
                        orderProduct.orderProductId,
                        orderProduct.price,
                        orderProduct.quantity,
                        product.productId,
                        product.image.storeFileName.as("image"),
                        product.description
                )
                .from(order)
                .join(order.orderProducts, orderProduct)
                .join(orderProduct.product, product)
                .where(order.orderUid.eq(orderUid))
                .fetch();

        if (result.isEmpty()) {
            return null;
        }

        OrderReadyPageDto orderReadyPageDto = null;

        for (Tuple tuple : result) {
            if (orderReadyPageDto == null) {
                orderReadyPageDto = new OrderReadyPageDto(
                        tuple.get(order.orderId),
                        tuple.get(order.member.memberId),
                        new ArrayList<>(),
                        tuple.get(order.orderDate),
                        tuple.get(order.status)
                );
            }

            OrderProductDto orderProductDto = new OrderProductDto(
                    tuple.get(orderProduct.orderProductId),
                    tuple.get(product.productId),
                    tuple.get(orderProduct.price),
                    tuple.get(orderProduct.quantity),
                    tuple.get(product.image.storeFileName.as("image")),
                    tuple.get(product.description)
            );

            orderReadyPageDto.getOrderProductsDto().add(orderProductDto);
        }

        return Optional.of(orderReadyPageDto);
    }

    @Override
    public Optional<Order> findOrderAndPaymentAndMember(String orderUid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .leftJoin(order.payment, payment).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<Order> findOrderAndPayment(String orderUid) {
        Order findOrder = queryFactory
                .selectFrom(order)
                .leftJoin(order.payment, payment)
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        return Optional.ofNullable(findOrder);
    }

    @Override
    public Optional<SuccessPageDto> getDeliveryDto(String orderUid) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Tuple result = queryFactory
                .select(
                        order.orderId,
                        payment.price,
                        delivery.address,
                        order.orderDate,
                        delivery.status,
                        payment.payMethod,
                        order.orderName
                )
                .from(order)
                .join(order.payment, payment)
                .join(order.delivery, delivery)
                .where(order.orderUid.eq(orderUid))
                .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        // 각 필드를 변환하여 SuccessPageDto 생성
        SuccessPageDto successPageDto = new SuccessPageDto(
                result.get(order.orderId),
                result.get(payment.price).longValue(),  // BigDecimal -> Long 변환
                result.get(delivery.address),
                result.get(order.orderDate).format(formatter),  // LocalDateTime -> String 변환
                result.get(delivery.status).name(),  // DeliveryStatus -> String 변환
                result.get(payment.payMethod),
                result.get(order.orderName)
        );

        return Optional.of(successPageDto);
    }

    @Override
    public Optional<OrderListGetDto> findAllOrdersByMemberId(Long memberId) {
        List<Tuple> fetch = queryFactory
                .select(
                        order.orderUid,
                        delivery.status,
                        payment.paidAt,
                        product.name,
                        orderProduct.price,
                        product.image.storeFileName)
                .from(order)
                .join(order.payment, payment)
                .join(order.orderProducts, orderProduct)
                .join(orderProduct.product, product)
                .join(order.delivery, delivery)
                .where(order.member.memberId.eq(memberId))
                .fetch();

        if (fetch.isEmpty()) {
            return Optional.empty();
        }

        OrderListGetDto orderListGetDto = new OrderListGetDto(
                fetch.get(0).get(order.orderUid),
                fetch.stream()
                        .map(tuple -> new OrderListPageDto(
                                tuple.get(delivery.status),
                                tuple.get(payment.paidAt),
                                tuple.get(product.name),
                                tuple.get(orderProduct.price),
                                tuple.get(product.image.storeFileName)
                        ))
                        .collect(Collectors.toList())
        );

        return Optional.of(orderListGetDto);
    }

    // 마이페이지 오더 상세 보기
    @Override
    public List<MyPageOrderDto> getMyPageOrderDtoByOrderUid(Long memberId, String orderUid) {
        StringExpression orderStatusDescription = getOrderStatusDescription().as("orderStatusDescription");
        StringExpression deliveryStatusDescription = getDeliveryStatusDescription().as("deliveryStatusDescription");

        return queryFactory
                .select(
                        orderStatusDescription,
                        payment.paidAt,
                        product.productId,
                        product.name,
                        product.image.storeFileName,
                        orderProduct.quantity,
                        orderProduct.price,
                        delivery.recipientName,
                        delivery.phoneNumber,
                        deliveryStatusDescription,
                        delivery.trackingNumber,
                        delivery.address,
                        delivery.deliveryRequest,
                        review.reviewId)
                .from(order)
                .join(order.payment, payment)
                .join(order.delivery, delivery)
                .join(order.orderProducts, orderProduct)
                .join(orderProduct.product, product)
                .leftJoin(review).on(review.product.productId.eq(product.productId)
                        .and(review.order.orderId.eq(order.orderId))
                        .and(review.member.memberId.eq(memberId)))
                .where(order.member.memberId.eq(memberId),
                        order.orderUid.eq(orderUid))
                .fetch()
                .stream()
                .map(tuple -> new MyPageOrderDto(
                        tuple.get(orderStatusDescription),
                        tuple.get(payment.paidAt),
                        tuple.get(product.productId),
                        tuple.get(product.name),
                        tuple.get(product.image.storeFileName),
                        tuple.get(orderProduct.quantity),
                        tuple.get(orderProduct.price),
                        tuple.get(delivery.recipientName),
                        tuple.get(delivery.phoneNumber),
                        tuple.get(deliveryStatusDescription),
                        tuple.get(delivery.trackingNumber),
                        tuple.get(delivery.address),
                        tuple.get(delivery.deliveryRequest),
                        tuple.get(review.reviewId) != null
                ))
                .toList();
    }

    // 조건 별 어드민 페이지 주문 리스트 가져오기 쿼리
    @Override
    public List<AdminOrderListDto> findAdminOrderListDtoByFilter(AdminOrderFilterDto filterDto) {
        return queryFactory
                .select(Projections.constructor(AdminOrderListDto.class,
                        order.orderId,
                        order.orderUid,
                        order.orderDate,
                        member.name,
                        getOrderStatusDescription(),
                        payment.price.longValue(),
                        getDeliveryStatusDescription()))
                .from(order)
                .leftJoin(order.member, member)
                .leftJoin(order.payment, payment)
                .leftJoin(order.delivery, delivery)
                .where(statusContains(filterDto.getStatus()),
                        orderDateContains(filterDto.getStartDate(), filterDto.getEndDate()))
                .fetch();
    }

    @Override
    public Optional<AdminOrderDetailDto> findAdminOrderDetailDtoList(Long orderId) {
        StringExpression orderStatusDescription = getOrderStatusDescription().as("orderStatusDescription");
        StringExpression deliveryStatusDescription = getDeliveryStatusDescription().as("deliveryStatusDescription");

        List<Tuple> fetch = queryFactory
                .select(order.orderId,
                        order.orderDate,
                        orderStatusDescription,
                        payment.price,
                        member.name,
                        member.email,
                        member.phoneNumber,
                        delivery.recipientName,
                        delivery.address,
                        delivery.deliveryRequest,
                        deliveryStatusDescription,
                        delivery.trackingNumber,
                        product.productId,
                        product.name,
                        orderProduct.price,
                        orderProduct.quantity,
                        product.image.storeFileName)
                .from(order)
                .leftJoin(order.member, member)
                .leftJoin(order.payment, payment)
                .leftJoin(order.delivery, delivery)
                .leftJoin(order.orderProducts, orderProduct)
                .leftJoin(orderProduct.product, product)
                .where(order.orderId.eq(orderId))
                .fetch();

        if (fetch.isEmpty()) {
            return Optional.empty();
        }

        Tuple firstTuple = fetch.get(0);
        AdminOrderDetailDto adminOrderDetailDto = new AdminOrderDetailDto(
                firstTuple.get(order.orderId),
                firstTuple.get(order.orderDate),
                firstTuple.get(orderStatusDescription),
                firstTuple.get(payment.price).longValue(),
                firstTuple.get(member.name),
                firstTuple.get(member.email),
                firstTuple.get(member.phoneNumber),
                firstTuple.get(delivery.recipientName),
                firstTuple.get(delivery.address),
                firstTuple.get(delivery.deliveryRequest),
                firstTuple.get(deliveryStatusDescription),
                firstTuple.get(delivery.trackingNumber),
                new ArrayList<>()
        );

        List<AdminProductDto> adminProductDtoList = fetch.stream()
                .map(tuple -> new AdminProductDto(
                        tuple.get(product.productId),
                        tuple.get(product.name),
                        tuple.get(orderProduct.price),
                        tuple.get(orderProduct.quantity),
                        tuple.get(product.image.storeFileName)
                )).toList();

        adminOrderDetailDto.setProducts(adminProductDtoList);

        return Optional.of(adminOrderDetailDto);
    }

    @Override
    public OrderProductReviewInfoDto findOrderProductReviewInfo(String orderUid, Long productId, Long memberId) {
        Tuple result = queryFactory
                .select(
                        product.productId,
                        product.name,
                        product.image.storeFileName,
                        review.reviewId,
                        review.rating,
                        review.comment
                )
                .from(order)
                .join(order.orderProducts, orderProduct)
                .join(orderProduct.product, product)
                .leftJoin(review).on(
                        review.product.productId.eq(product.productId)
                                .and(review.order.orderId.eq(order.orderId))
                                .and(review.member.memberId.eq(memberId))
                )
                .where(order.member.memberId.eq(memberId),
                        order.orderUid.eq(orderUid),
                        product.productId.eq(productId))
                .fetchOne();

        if (result == null) {
            throw new EntityNotFoundException("해당 주문 또는 리뷰 정보를 찾을 수 없습니다.");
        }

        boolean isReviewed = result.get(review.reviewId) != null;

        // 리뷰가 있는 경우와 없는 경우에 따라 DTO에 넣을 데이터를 다르게 설정
        return new OrderProductReviewInfoDto(
                isReviewed,
                result.get(product.productId),
                result.get(product.name),
                result.get(product.image.storeFileName),
                isReviewed ? result.get(review.rating) : null,
                isReviewed ? result.get(review.comment) : null
        );
    }

    private static StringExpression getOrderStatusDescription() {
        return new CaseBuilder()
                .when(order.status.eq(OrderStatus.READY)).then(OrderStatus.READY.getDescription())
                .when(order.status.eq(OrderStatus.ING)).then(OrderStatus.ING.getDescription())
                .when(order.status.eq(OrderStatus.COMPLETE)).then(OrderStatus.COMPLETE.getDescription())
                .when(order.status.eq(OrderStatus.CANCEL)).then(OrderStatus.CANCEL.getDescription())
                .otherwise("조회 불가");
    }

    private static StringExpression getDeliveryStatusDescription() {
        return new CaseBuilder()
                .when(delivery.status.eq(DeliveryStatus.READY)).then(DeliveryStatus.READY.getDescription())
                .when(delivery.status.eq(DeliveryStatus.ING)).then(DeliveryStatus.ING.getDescription())
                .when(delivery.status.eq(DeliveryStatus.COMPLETE)).then(DeliveryStatus.COMPLETE.getDescription())
                .otherwise("조회 불가");
    }

    private BooleanExpression statusContains(String status) {
        return status == null ? null : order.status.stringValue().contains(status);
    }

    private BooleanExpression orderDateContains(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return null;
        }

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        if (startDateTime != null && endDateTime != null) {
            return order.orderDate.between(startDateTime, endDateTime);
        } else if (startDateTime != null) {
            return order.orderDate.goe(startDateTime);
        } else {
            return order.orderDate.loe(endDateTime);
        }
    }

}
