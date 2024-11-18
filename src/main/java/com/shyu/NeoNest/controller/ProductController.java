package com.shyu.NeoNest.controller;

import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.dto.request.ProductEditDto;
import com.shyu.NeoNest.dto.request.ProductRegisterDto;
import com.shyu.NeoNest.dto.response.AdminProductListDto;
import com.shyu.NeoNest.dto.response.EditProductDto;
import com.shyu.NeoNest.dto.response.ProductDto;
import com.shyu.NeoNest.security.CustomUserDetails;
import com.shyu.NeoNest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping("/products")
    public ResponseEntity<Map<String, Object>> registerProduct(@RequestParam("image") MultipartFile image,
                                                               @Validated @RequestPart("product") ProductRegisterDto productDto) throws IOException {
        productService.saveProduct(image, productDto);

        return ResponseEntity.ok(Map.of("success", true, "message", "상품 등록 성공"));
    }

    // 상품 수정
    @PatchMapping("/products/{productId}")
    public ResponseEntity<Map<String, Object>> editProduct(@PathVariable("productId") Long productId,
                                                           @RequestParam(value = "image", required = false) MultipartFile image,
                                                           @Validated @RequestPart("product") ProductEditDto productEditDto) throws IOException {
        productService.editProduct(productId, image, productEditDto);

        return ResponseEntity.ok(Map.of("success", true, "message", "상품 수정 성공"));
    }

    // 상품 조회
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam("category") String category,
                                                        @RequestParam("sort") String sortCond) {
        List<ProductDto> products = productService.getProducts(category, sortCond);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // 상품 리스트 목록에서 상품 클릭 시 상세정보 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDto> getProducts(@PathVariable("productId") Long productId) {
        ProductDto getProduct = productService.getProduct(productId);

        return new ResponseEntity<>(getProduct, HttpStatus.OK);
    }

    // 이미지
    @ResponseBody
    @GetMapping("/products/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {

        return productService.getProductImage(filename);
    }

    // 관리자 페이지 상품 관리(상품 목록들) 조회
    @GetMapping("/admin/products")
    public ResponseEntity<List<AdminProductListDto>> getAdminProductListDto() {
        List<AdminProductListDto> adminProductList = productService.getAdminProductList();

        return new ResponseEntity<>(adminProductList, HttpStatus.OK);
    }

    // 관리자 페이지 상품 수정 DTO 조회
    @GetMapping("/admin/products/{productId}")
    public ResponseEntity<EditProductDto> getAdminProduct(@PathVariable("productId") Long productId) {
        EditProductDto adminProduct = productService.getAdminProduct(productId);

        return new ResponseEntity<>(adminProduct, HttpStatus.OK);
    }

}
