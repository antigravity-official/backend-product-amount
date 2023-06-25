package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");
    }

    @DisplayName("Final 상품 가격 추출 단위테스트")
    @Test
    void finalGetProductAmount() {
        int[] couponIds = {1, 2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        ProductAmountResponse response = productService.getProductAmount(request);
        Assertions.assertThat(response.getDiscountPrice()).isEqualTo(32250);
        Assertions.assertThat(response.getFinalPrice()).isEqualTo(180000);
    }

    @DisplayName("프로모션 날짜 변환 테스트")
    @Test
    void convertDateToLocalDate() {
        int promotionId = 1;

        Promotion promotion = promotionRepository.getPromotion(promotionId);

        LocalDate useStartedAt = Instant.ofEpochMilli(promotion.getUse_started_at().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate useEndedAt = Instant.ofEpochMilli(promotion.getUse_ended_at().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        Assertions.assertThat(useStartedAt).isEqualTo(LocalDate.of(2022, 11, 1));
        Assertions.assertThat(useEndedAt).isEqualTo(LocalDate.of(2023, 3, 1));
    }
}