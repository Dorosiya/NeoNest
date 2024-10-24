package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.domain.Order;
import com.shyu.NeoNest.dto.request.AdminOrderFilterDto;
import com.shyu.NeoNest.dto.request.OrderCreateDto;
import com.shyu.NeoNest.dto.response.*;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class OrderController {

    private final OrderService orderService;

    // 장바구니 -> 주문 or 상품상세 -> 구매 시 주문 생성
    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        log.info("주문 생성");
        String orderUid = orderService.createOrder(orderCreateDto);

        return new ResponseEntity<>(orderUid, HttpStatus.CREATED);
    }

    // 주문 시 오더 조회
    @GetMapping("/orders/{orderUid}")
    public ResponseEntity<OrderReadyPageDto> getOrder(@PathVariable("orderUid") String orderUid) {
        log.info("주문 시 오더 조회");
        OrderReadyPageDto getOrder = orderService.getOrder(orderUid);

        return new ResponseEntity<>(getOrder, HttpStatus.OK);
    }

    // 마이페이지 오더 목록 조회
    @GetMapping("/mypage/orders")
    public ResponseEntity<OrderListGetDto> getOrderList(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("마이페이지 오더 목록 조회");
        Long memberId = customUserDetails.getMemberId();
        OrderListGetDto orderListPageDto = orderService.getOrderListPageDto(memberId);

        return new ResponseEntity<>(orderListPageDto, HttpStatus.OK);
    }

    // 마이페이지 상세 오더 조회
    @GetMapping("/mypage/orders/{orderUid}")
    public ResponseEntity<List<MyPageOrderDto>> getMyPageOrderDto(@PathVariable String orderUid,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("마이페이지 상세 오더 조회");
        Long memberId = customUserDetails.getMemberId();
        List<MyPageOrderDto> myOrderPageDto = orderService.getMyOrderPageDto(memberId, orderUid);

        return new ResponseEntity<>(myOrderPageDto, HttpStatus.OK);
    }

    //어드민 페이지 오더 조회
    @GetMapping("/admin/orders")
    public ResponseEntity<List<AdminOrderListDto>> getAdminOrders(@ModelAttribute("AdminOrderFilterDto") AdminOrderFilterDto adminOrderFilterDto) {
        log.info("어드민 페이지 오더 조회");
        List<AdminOrderListDto> adminOrderListDto = orderService.findAdminOrderListDto(adminOrderFilterDto);
        return new ResponseEntity<>(adminOrderListDto, HttpStatus.OK);
    }

    //어드민 페이지 오더 수정
    @PatchMapping("/admin/orders/{orderId}/orderStatus")
    public ResponseEntity<Map<String, Object>> editOrderStatus(@PathVariable Long orderId,
                                                               @RequestBody Map<String, String> requestBody) {
        log.info("어드민 페이지 오더 조회");

        String status = requestBody.get("status");
        orderService.changeOrderStatus(orderId, status);

        return ResponseEntity.ok(Map.of("message", "오더 수정 성공"));
    }

    //어드민 페이지 오더 상세 정보 조회
    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<AdminOrderDetailDto> getOrderDetail(@PathVariable Long orderId) {
        log.info("어드민 페이지 오더 상세 정보 조회");

        AdminOrderDetailDto adminOrderDetailDtoList = orderService.findAdminOrderDetailDtoList(orderId);

        return new ResponseEntity<>(adminOrderDetailDtoList, HttpStatus.OK);
    }



}
