package com.shyu.NeoNest.repository;

import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.dto.request.AdminOrderFilterDto;
import com.shyu.NeoNest.dto.response.*;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    Optional<Order> findOrderByUid(String Uid);

    Optional<OrderReadyPageDto> getOrderDto(String orderUid);

    Optional<Order> findOrderAndPaymentAndMember(String orderUid);

    Optional<Order> findOrderAndPayment(String orderUid);

    Optional<SuccessPageDto> getDeliveryDto(String orderUid);

    Optional<OrderListGetDto> findAllOrdersByMemberId(Long memberId);

    List<MyPageOrderDto> getMyPageOrderDtoByOrderUid(Long memberId, String orderUid);

    List<AdminOrderListDto> findAdminOrderListDtoByFilter(AdminOrderFilterDto filterDto);

    Optional<AdminOrderDetailDto> findAdminOrderDetailDtoList(Long orderId);

    ReviewInfoDto findReviewInfo(String orderUid, Long productId, Long memberId);

}
