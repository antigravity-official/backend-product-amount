package antigravity.repository;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotion.enums.PromotionType;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaDbConnectionTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionProductsRepository promotionProductsRepository;

    @AfterEach
    void tearDown() {
        promotionProductsRepository.deleteAllInBatch();
        promotionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("JPA DB 연결 테스트")
    @Test
    void jpa_DB_연결_테스트() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(30_000)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        Promotion code = Promotion.builder()
                .promotionType(PromotionType.CODE)
                .name("15% 할인코드")
                .discountType(DiscountType.PERCENT)
                .discountValue(15)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.saveAll(List.of(coupon, code));

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        PromotionProducts productCode = PromotionProducts.builder()
                .product(product)
                .promotion(code)
                .build();
        promotionProductsRepository.saveAll(List.of(productCoupon, productCode));

        //when
        PromotionProducts promotionProducts = promotionProductsRepository.findById(1).get();

        //then
        assertThat(promotionProducts.getProduct().getName()).isEqualTo("피팅노드상품");
        assertThat(promotionProducts.getPromotion().getName()).isEqualTo("30000원 할인쿠폰");

    }

}