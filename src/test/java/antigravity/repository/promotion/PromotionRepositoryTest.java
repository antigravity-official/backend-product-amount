package antigravity.repository.promotion;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.base.RepositoryTestSupport;
import antigravity.global.fixture.PromotionFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static antigravity.global.fixture.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.fixture.PromotionFixture.VALID_FIX_PROMOTION1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[Repository] PromotionRepository - Data Jpa Test")
class PromotionRepositoryTest extends RepositoryTestSupport {

    @Autowired
    PromotionRepository promotionRepository;

    @Test
    @DisplayName("[save] DB에 Product를 저장한다.")
    void When_ValidParameterRequested_Expect_save() throws Exception {
        //given && when
        Promotion savedPromotion = promotionRepository.save(VALID_FIX_PROMOTION1.toEntity());
        //then
        assertAll(
                () -> assertThat(savedPromotion.getPromotionType()).isEqualTo("COUPON"),
                () -> assertThat(savedPromotion.getName()).isEqualTo("30000원 할인쿠폰"),
                () -> assertThat(savedPromotion.getDiscountType()).isEqualTo("WON"),
                () -> assertThat(savedPromotion.getDiscountValue()).isEqualTo(30000),
                () -> assertThat(savedPromotion.getUseStartedAt()).isEqualTo(LocalDate.parse("2022-11-01")),
                () -> assertThat(savedPromotion.getUseEndedAt()).isEqualTo(LocalDate.parse("2099-06-30"))
        );
    }
}
