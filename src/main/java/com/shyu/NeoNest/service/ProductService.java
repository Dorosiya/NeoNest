package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Category;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.ProductCategory;
import com.shyu.NeoNest.domain.UploadFile;
import com.shyu.NeoNest.dto.request.ProductEditDto;
import com.shyu.NeoNest.dto.request.ProductRegisterDto;
import com.shyu.NeoNest.dto.response.AdminProductListDto;
import com.shyu.NeoNest.dto.response.EditProductDto;
import com.shyu.NeoNest.dto.response.ProductDto;
import com.shyu.NeoNest.exception.ProductNotFoundException;
import com.shyu.NeoNest.repository.CategoryRepository;
import com.shyu.NeoNest.repository.ProductCategoryRepository;
import com.shyu.NeoNest.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final FileService fileService;
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;

    // 상품 등록
    @Transactional
    public void saveProduct(MultipartFile image, ProductRegisterDto dto) throws IOException {
        Category findCategory = categoryRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 파일 저장
        UploadFile uploadImage;
        try {
            uploadImage = fileService.storeFile(image);
        } catch (IOException e) {
            throw new IllegalStateException("파일 저장에 실패하였습니다.", e);
        }

        Product registerProduct = createProduct(dto, uploadImage);
        productRepository.save(registerProduct);

        ProductCategory registerProductCategory = createProductCategory(findCategory, registerProduct);
        productCategoryRepository.save(registerProductCategory);
    }

    private static ProductCategory createProductCategory(Category findCategory, Product registerProduct) {
        ProductCategory registerProductCategory = ProductCategory.builder()
                .category(findCategory)
                .product(registerProduct)
                .build();
        return registerProductCategory;
    }

    private static Product createProduct(ProductRegisterDto dto, UploadFile uploadImage) {
        Product registerProduct = Product.builder()
                .name(dto.getName())
                .originalPrice(dto.getPrice())
                .discountRate(0)
                .stockQuantity(dto.getStockQuantity())
                .description(dto.getDescription())
                .image(uploadImage)
                .build();
        return registerProduct;
    }

    // 상품 수정
    @Transactional
    public void editProduct(Long productId, MultipartFile image, ProductEditDto dto) throws IOException {
        Category findCategory = categoryRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID입니다."));

        // 새 이미지가 있을 경우 기존 이미지 삭제 후 새 이미지 저장
        /*if (image != null && !image.isEmpty()) {
            String existingImageFilename = product.getImage().getStoreFileName();
            if (existingImageFilename != null) {
                // 기존 파일 삭제
                fileService.deleteFile(existingImageFilename);
            }

            // 새로운 이미지 저장
            UploadFile uploadImage = fileService.storeFile(image);
            product.editProduct(
                    dto.getName(),
                    dto.getPrice(),
                    dto.getStockQuantity(),
                    dto.getDescription(),
                    uploadImage);
        } else {
            // 이미지 변경이 없는 경우 나머지 정보만 수정
            product.editProduct(
                    dto.getName(),
                    dto.getPrice(),
                    dto.getStockQuantity(),
                    dto.getDescription(),
                    null); // 기존 이미지 유지
        }*/
        UploadFile updatedImage = null;
        if (image != null && !image.isEmpty()) {
            String existingImageFilename = product.getImage().getStoreFileName();
            if (existingImageFilename != null) {
                // 기존 파일 삭제
                fileService.deleteFile(existingImageFilename);
            }

            // 새로운 이미지 저장
            updatedImage = fileService.storeFile(image);
        }

        // 이미지 변경이 없는 경우 나머지 정보만 수정
        product.editProduct(
                dto.getName(),
                dto.getPrice(),
                dto.getStockQuantity(),
                dto.getDescription(),
                updatedImage != null ? updatedImage : product.getImage()); // 기존 이미지 유지


        // 상품의 카테고리 업데이트
        ProductCategory productCategory = productCategoryRepository.findByProduct_ProductId(product.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("존재하지 않는 상품 ID입니다."));
        productCategory.editCategory(findCategory);

        // 저장
        productRepository.save(product);
        productCategoryRepository.save(productCategory);
    }

    @Transactional(readOnly = true)
    public ProductDto getProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("유효한 상품 Id를 입력해주세요.");
        }

        return productRepository.getByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
    }

    // 상품 조회(sort 조건 포함)
    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(String categoryCond, String sortCond) {
        return productRepository.searchByCond(categoryCond, sortCond);
    }

    // 상품 조회 시 이미지가 있다면 가져오기
    @Transactional(readOnly = true)
    public Resource getProductImage(String filename) throws MalformedURLException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("filename은 필수 입니다.");
        }

        return new UrlResource("file:" + fileService.getFullPath(filename));
    }

    // 상품 관리 페이지 상품 조회
    @Transactional(readOnly = true)
    public List<AdminProductListDto> getAdminProductList() {
        return productRepository.getAdminProductListDto();
    }

    // 상품 관리 페이지 상품 수정 시 기존 데이터 가져오기
    @Transactional(readOnly = true)
    public EditProductDto getAdminProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("유효한 상품 Id를 입력해주세요.");
        }

        return productRepository.getEditProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품 ID입니다."));
    }

}
