package com.shyu.NeoNest.domain;

import com.shyu.NeoNest.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
@Entity
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    /*private Long price;*/

    private Long originalPrice; // price -> originalPrice (원래 가격)

    private int discountRate; // 할인율 (% 표현)

    private Long discountPrice; // 할인 가격 (할인이 적용된 가격 저장)

    private int stockQuantity;

    private String description;

    @Embedded
    private UploadFile image;

    @Builder
    private Product(String name, Long originalPrice, int discountRate, int stockQuantity, String description, UploadFile image) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.image = image;
    }

    public void editProduct(String name, Long price, int stockQuantity, String description, UploadFile image) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.discountRate = discountRate;
        this.discountPrice = calculateDiscountPrice();
        this.stockQuantity = stockQuantity;
        this.description = description;

        if (image != null) {
            this.image = image; // 새로운 이미지가 있으면 설정
        }
    }

    public void changePrice(Long changePrice) {
        this.originalPrice = changePrice;
        this.discountPrice = calculateDiscountPrice();
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 충분하지 않습니다.");
        }
        this.stockQuantity = restStock;
    }

    private Long calculateDiscountPrice() {
        if (discountRate > 0) {
            return originalPrice - (originalPrice * discountRate / 100);
        }
        return originalPrice;
    }

}
