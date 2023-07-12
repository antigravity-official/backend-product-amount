package antigravity.repository.promotionproducts;

import antigravity.domain.PromotionProducts;
import antigravity.global.base.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.domain.PromotionProducts.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[Repository] PromotionProductsRepository - Data Jpa Test")
class PromotionProductsRepositoryTest extends RepositoryTestSupport {

    @Autowired
    PromotionProductsRepository promotionProductsRepository;

    @Test
    @DisplayName("[save] DB에 Product를 저장한다.")
    void When_ValidParameterRequested_Expect_save() throws Exception {
        //given && when
        PromotionProducts savedMapping = promotionProductsRepository.save(of(1, 2));
        //then
        assertAll(
                () -> assertThat(savedMapping.getPromotionId()).isEqualTo(1),
                () -> assertThat(savedMapping.getProductId()).isEqualTo(2)
        );
    }
}
