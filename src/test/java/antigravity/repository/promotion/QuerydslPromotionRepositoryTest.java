package antigravity.repository.promotion;

import antigravity.config.QuerydslConfig;
import antigravity.domain.promotion.Promotion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static antigravity.domain.promotion.PromotionFixture.aPromotion;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import({
    QuerydslConfig.class,
    QuerydslPromotionRepository.class
})
@ActiveProfiles("h2")
@DataJpaTest
public class QuerydslPromotionRepositoryTest {
    @Autowired
    QuerydslPromotionRepository querydslPromotionRepository;

    @Test
    @DisplayName("infrastructure(mysql)에 해당 상품에 대해 기간내에 사용 가능한 프로모션 정보가 1개 존재할 경우")
    void findByProductIdAndPromotionIdsAndUseAtBetweenTest_1() {
        // given
        Promotion savePromotion_1 = querydslPromotionRepository.save(
                aPromotion()
                        .useStartedAt(now().minusDays(10))
                        .useEndedAt(now().plusDays(10))
                        .productIds(asList(1L))
                        .build()
        );
        // 프로모션 적용 불가능
        // 기간 내에 사용 불가능
        Promotion savePromotion_2 = querydslPromotionRepository.save(
                aPromotion()
                        .useStartedAt(now().minusDays(11))
                        .useEndedAt(now().minusDays(10))
                        .productIds(asList(1L))
                        .build()
        );
        Promotion savePromotion_3 = querydslPromotionRepository.save(
                aPromotion()
                        .useStartedAt(now().plusDays(1))
                        .useEndedAt(now().plusDays(10))
                        .productIds(asList(1L))
                        .build()
        );
        // 1번 상품에 대해 적용 가능한 프로모션이 아님
        Promotion savePromotion_4 = querydslPromotionRepository.save(
                aPromotion()
                        .useStartedAt(now().minusDays(10))
                        .useEndedAt(now().plusDays(10))
                        .productIds(asList(2L))
                        .build()
        );
        ////////////////////////////////////////////////////////////////////////////////////////////

        // when
        List<Promotion> promotions = querydslPromotionRepository.findByProductIdAndPromotionIdsAndUseAtBetween(
                1L,
                asList(savePromotion_1.getId(), savePromotion_2.getId(), savePromotion_3.getId(), savePromotion_4.getId()),
                now()
        );

        // then
        assertEquals(1, promotions.size());
    }
}
