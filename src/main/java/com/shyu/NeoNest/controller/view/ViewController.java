package com.shyu.NeoNest.controller.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    /*@Value("${spring.portOne.impCode}")
    private String impCode;*/

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/products")
    public String productListPage(@RequestParam("category") String category) {
        return "product-list";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/products/{productId}")
    public String productDetailPage(@PathVariable Long productId) {
        return "product-detail";
    }

    @GetMapping("/order/{orderUid}")
    public String orderPage(@PathVariable String orderUid) {
        return "order";
    }

    // 결제 성공 시 보여주는 페이지
    @GetMapping("/payment/success/{orderUid}")
    public String paymentSuccessPage(@PathVariable String orderUid) {
        return "payment-success";
    }

    // 마이 페이지
    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    // 마이 페이지 -
    @GetMapping("/mypage/profile")
    public String myPageProfile() {
        return "profile";
    }

    // 마이 페이지 - 주문 확인 페이지
    @GetMapping("/mypage/orders")
    public String myPageOrders() {
        return "order-list";
    }

    // 마이 페이지 - 주문 상세 보기 페이지
    @GetMapping("/mypage/orders/{orderUid}")
    public String myPageOrder(@PathVariable String orderUid) {
        return "order-detail";
    }

    // 마이 페이지 - 리뷰 작성 페이지
    @GetMapping("/mypage/reviews/{orderUid}/{productId}")
    public String myPageOrder(@PathVariable String orderUid,
                              @PathVariable String productId) {
        return "review-create";
    }



    // 관리자 페이지 - 메인 페이지
    @GetMapping("/admin/main")
    public String adminMainPage() {
        return "admin-main";
    }

    // 관리자 페이지 - 상품 등록 페이지
    @GetMapping("/admin/products/new")
    public String productFormPage() {
        return "admin-products-register-form";
    }

    // 관리자 페이지 - 상품 관리 페이지
    @GetMapping("/admin/products")
    public String productListPage() {
        return "admin-products-management";
    }

    // 관리자 페이지 - 상품 수정 페이지
    @GetMapping("/admin/products/edit/{productId}")
    public String productListPage(@PathVariable Long productId) {
        return "admin-products-edit-form";
    }

    // 관리자 페이지 - 주문 관리 페이지
    @GetMapping("/admin/orders")
    public String orderListPage() {
        return "admin-orders-list";
    }

    // 관리자 페이지 - 상세 주문 관리 페이지
    @GetMapping("/admin/orders/{orderId}")
    public String orderListPage(@PathVariable Long orderId) {
        return "admin-orders-detail";
    }

}
