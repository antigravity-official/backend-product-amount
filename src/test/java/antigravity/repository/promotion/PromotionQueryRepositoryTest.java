package antigravity.repository.promotion;

import antigravity.domain.Promotion;
import antigravity.global.base.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static antigravity.global.fixture.PromotionFixture.VALID_FIX_PROMOTION1;
import static antigravity.global.fixture.PromotionFixture.VALID_RATE_PROMOTION2;
import static java.util.List.of;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("[Repository] ProductQueryRepository - Data Jpa Test")
class PromotionQueryRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionQueryRepository promotionQueryRepository;

    @Test
    @DisplayName("[findById] JPA - findById를 정상적으로 호출해, Promotion을 리턴합니다.")
    void When_FindById_Expect_Product() throws Exception {
        //given
        Promotion promotion = VALID_FIX_PROMOTION1.toEntity();
        //when
        Promotion savedPromotion = promotionRepository.save(promotion);
        Promotion foundPromotion = promotionQueryRepository.findById(promotion.getId()).get();
        //then
        assertAll(
                () -> assertThat(savedPromotion).isSameAs(foundPromotion),
                () -> assertThat(savedPromotion.getId()).isEqualTo(foundPromotion.getId()),
                () -> assertThat(savedPromotion.getDiscountType()).isEqualTo(foundPromotion.getDiscountType()),
                () -> assertThat(savedPromotion.getName()).isEqualTo(foundPromotion.getName()),
                () -> assertThat(savedPromotion.getDiscountValue()).isEqualTo(foundPromotion.getDiscountValue()),
                () -> assertThat(savedPromotion.getUseStartedAt()).isEqualTo(foundPromotion.getUseStartedAt()),
                () -> assertThat(savedPromotion.getUseEndedAt()).isEqualTo(foundPromotion.getUseEndedAt())
        );
    }

    @Test
    @DisplayName("[findPromotionByIds] JPA - 프로모션 아이디 리스트 요청에 맞는, 프로모션 리스트를 리턴합니다.")
    void When_FindPromotionByIds_Expect_PromotionList() throws Exception {
        //given
        List<Promotion> promotions = of(VALID_FIX_PROMOTION1.toEntity(), VALID_RATE_PROMOTION2.toEntity());
        //when
        Promotion savedPromotion1 = promotionRepository.save(VALID_FIX_PROMOTION1.toEntity());
        Promotion savedPromotion2 = promotionRepository.save(VALID_RATE_PROMOTION2.toEntity());
        List<Integer> promotionIds = of(savedPromotion1.getId(), savedPromotion2.getId());
        List<Promotion> foundPromotions = promotionQueryRepository.findPromotionsByIds(promotionIds);
        //then
        assertAll(() -> {
            range(0, promotions.size()).forEach(i -> {
                Promotion expected = promotions.get(i);
                Promotion actual = foundPromotions.get(i);
                assertThat(actual.getName()).isEqualTo(expected.getName());
                assertThat(actual.getDiscountType()).isEqualTo(expected.getDiscountType());
                assertThat(actual.getDiscountValue()).isEqualTo(expected.getDiscountValue());
                assertThat(actual.getUseStartedAt()).isEqualTo(expected.getUseStartedAt());
                assertThat(actual.getUseEndedAt()).isEqualTo(expected.getUseEndedAt());
            });
        });
    }
}
