package com.shyu.NeoNest.service;

import com.shyu.NeoNest.domain.Member;
import com.shyu.NeoNest.domain.Product;
import com.shyu.NeoNest.domain.Role;
import com.shyu.NeoNest.enums.RoleType;
import com.shyu.NeoNest.repository.MemberRepository;
import com.shyu.NeoNest.repository.ProductRepository;
import com.shyu.NeoNest.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private Role userRole;
    private Member testMember;

    @BeforeEach
    void setup() {
        userRole = Role.builder()
                .roleName(RoleType.ROLE_USER)
                .build();
        roleRepository.save(userRole);

        testMember = Member.builder()
                .role(userRole)
                .memberName("testUser1")
                .password("testUser1")
                .age(21)
                .email("testUser1@naver.com")
                .build();
        memberRepository.save(testMember);
    }

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
        roleRepository.deleteAll();

    }

    @DisplayName("상품 등록 테스트")
    @Test
    void registerProduct() {
        // given
        Product newProduct = Product.builder()
                .name("첨단마우스")
                .price(10000L)
                .stockQuantity(10)
                .build();

        productRepository.save(newProduct);

        // when
        Product findProduct = productRepository.findByName("첨단마우스")
                .orElseThrow(() -> new RuntimeException("오류입니다."));

        // then
        Assertions.assertThat(findProduct.getName()).isEqualTo("첨단마우스");
        Assertions.assertThat(findProduct.getPrice()).isEqualTo(10000L);
        Assertions.assertThat(findProduct.getStockQuantity()).isEqualTo(10);
    }

}