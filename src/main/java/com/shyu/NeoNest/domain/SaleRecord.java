package com.shyu.NeoNest.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class SaleRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SaleRecordId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime saleDate;
    private int quantity;

}
