package com.shyu.NeoNest.domain;

import com.shyu.NeoNest.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 사용자

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>(); // 주문별 상품 N-N 중간 테이블

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", referencedColumnName = "delivery_id")
    private Delivery delivery; // 배송

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment; // 결제

    private String orderName; // 주문 이름

    private String orderUid; //

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder
    private Order(Member member, List<OrderProduct> orderProducts, LocalDateTime orderDate, String orderName, String orderUid, OrderStatus status, Payment payment) {
        this.member = member;
        this.orderProducts = orderProducts != null ? orderProducts : new ArrayList<>();
        this.payment = payment;
        this.orderName = orderName;
        this.orderUid = orderUid;
        this.orderDate = orderDate;
        this.status = status;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProducts.add(orderProduct);
        orderProduct.setOrder(this);
    }

    public void addPayment(Payment payment) {
        this.payment = payment;
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public List<String> getProductNames() {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getProduct().getName())
                .collect(Collectors.toList());
    }

}
