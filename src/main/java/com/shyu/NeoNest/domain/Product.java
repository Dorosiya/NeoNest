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

    private Long price;

    private int stockQuantity;

    private String description;

    @Embedded
    private UploadFile image;

    @Builder
    private Product(String name, Long price, int stockQuantity, String description, UploadFile image) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.image = image;
    }

    public void editProduct(String name, Long price, int stockQuantity, String description, UploadFile image) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;

        if (image != null) {
            this.image = image; // 새로운 이미지가 있으면 설정
        }
    }

    public void changePrice(Long changePrice) {
        this.price = changePrice;
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

}
