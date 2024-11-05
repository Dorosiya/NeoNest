package com.shyu.NeoNest.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.QMember;
import com.shyu.NeoNest.domain.QReview;
import com.shyu.NeoNest.dto.response.AdminProductListDto;
import com.shyu.NeoNest.dto.response.EditProductDto;
import com.shyu.NeoNest.dto.response.ProductDto;
import com.shyu.NeoNest.dto.response.ProductReviewInfoDto;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static com.shyu.NeoNest.domain.QCategory.category;
import static com.shyu.NeoNest.domain.QMember.member;
import static com.shyu.NeoNest.domain.QProduct.product;
import static com.shyu.NeoNest.domain.QProductCategory.productCategory;
import static com.shyu.NeoNest.domain.QReview.review;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Product> findByName(String name) {
        Product findProduct = queryFactory
                .selectFrom(product)
                .where(product.name.eq(name))
                .fetchOne();

        return Optional.ofNullable(findProduct);
    }

//    @Override
//    public List<Product> searchByCond(String categoryCond, String sort) {
//        List<Product> searchProduct = queryFactory
//                .select(product)
//                .from(product)
//                .join(productCategory).on(product.productId.eq(productCategory.product.productId))
//                .join(category).on(productCategory.category.categoryId.eq(category.categoryId))
//                .where(category.categoryName.eq(categoryCond))
//                .fetch();
//
//        return searchProduct;
//    }

    @Override
    public List<ProductDto> searchByCond(String categoryCond, String sortCond) {

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortCond);

        List<ProductDto> searchProduct = queryFactory
                .select(Projections.constructor(ProductDto.class,
                        product.productId,
                        product.name,
                        product.originalPrice,
                        product.description,
                        product.image.storeFileName.as("image") // 이미지 필드 매핑
                ))
                .from(product)
                .join(productCategory).on(product.productId.eq(productCategory.product.productId))
                .join(category).on(productCategory.category.categoryId.eq(category.categoryId))
                .where(category.categoryName.eq(categoryCond))
                .orderBy(orderSpecifier)
                .fetch();

        return searchProduct;
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortCond) {

        return switch (sortCond) {
            case "new" -> product.lastModifiedDate.desc();
            case "price_asc" -> product.originalPrice.asc();
            case "price_desc" -> product.originalPrice.desc();
            default -> product.lastModifiedDate.desc();
        };
    }

    @Override
    public Optional<ProductDto> getByProductId(Long productId) {
        ProductDto getProductDto = queryFactory
                .select(Projections.constructor(ProductDto.class,
                        product.productId,
                        product.name,
                        product.originalPrice,
                        product.description,
                        product.image.storeFileName.as("image")
                ))
                .from(product)
                .where(product.productId.eq(productId))
                .fetchOne();

        return Optional.ofNullable(getProductDto);
    }

    @Override
    public List<Product> getProducts(List<Long> productIds) {
        return queryFactory
                .selectFrom(product)
                .where(product.productId.in(productIds))
                .fetch();
    }

    @Override
    public List<AdminProductListDto> getAdminProductListDto() {
        return queryFactory
                .select(Projections.constructor(AdminProductListDto.class,
                        product.productId,
                        product.name,
                        product.originalPrice,
                        product.stockQuantity,
                        product.image.storeFileName))
                .from(product)
                .fetch();
    }

    @Override
    public Optional<EditProductDto> getEditProductById(Long productId) {
        EditProductDto editProductDto = queryFactory
                .select(Projections.constructor(EditProductDto.class,
                        product.productId,
                        product.name,
                        product.description,
                        product.originalPrice,
                        product.stockQuantity,
                        product.image.uploadFileName,
                        product.image.storeFileName,
                        category.categoryName))
                .from(product)
                .join(productCategory).on(product.productId.eq(productCategory.product.productId))
                .join(productCategory.category, category)
                .where(product.productId.eq(productId))
                .fetchOne();

        return Optional.ofNullable(editProductDto);
    }

}
