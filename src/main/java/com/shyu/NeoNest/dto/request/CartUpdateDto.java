package com.shyu.NeoNest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartUpdateDto {

    @NotNull(message = "회원 ID는 필수 값 입니다.")
    private Long memberId; // 멤버아이디를 받을 것인지 인증정보를 사용할 것인지 고민

    @NotNull(message = "장바구니 ID는 필수 값 입니다.")
    private Long cartId;

    @NotNull(message = "상품 ID는 필수 값 입니다.")
    private Long productId;

    @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
    @Max(value = 100, message = "수량은 최대 100 이하여야 합니다.")
    private int quantity;

}
