package com.shyu.NeoNest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Payment;
import com.shyu.NeoNest.domain.QPayment;
import jakarta.persistence.EntityManager;

import java.util.Optional;

import static com.shyu.NeoNest.domain.QPayment.payment;

public class PaymentRepositoryImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PaymentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /*@Override
    public Optional<Payment> findByPaymentUid(String PaymentUid) {
        Payment findPayment = queryFactory
                .selectFrom(payment)
                .where(payment.paymentUid.eq(PaymentUid))
                .fetchOne();

        return Optional.ofNullable(findPayment);
    }*/
}
