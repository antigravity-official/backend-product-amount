package antigravity.repository.promotionproducts;

import antigravity.domain.PromotionProducts;
import antigravity.global.base.RepositoryTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static antigravity.domain.PromotionProducts.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[Repository] PromotionProductsQueryRepository - Data Jpa Test")
class PromotionProductsQueryRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private PromotionProductsRepository promotionProductsRepository;

    @Autowired
    private PromotionProductsQueryRepository promotionProductsQueryRepository;

    @Test
    @DisplayName("[findPromotionIdsByProductId] JPA - 프로모션 아이디 리스트 요청에 맞는, 프로모션 리스트를 리턴합니다.")
    void When_FindPromotionByIdsByProductId_Expect_PromotionIds() throws Exception {
        //given
        final int productId = -35;
        //when
        PromotionProducts savedMapping1 = promotionProductsRepository.save(of(1, productId));
        PromotionProducts savedMapping2 = promotionProductsRepository.save(of(2, productId));
        PromotionProducts expectNotfound = promotionProductsRepository.save(of(3, 2));

        List<Integer> foundPromotionIds = promotionProductsQueryRepository.findPromotionIdsByProductId(productId);

        //then
        assertAll(
                () -> assertThat(foundPromotionIds.size()).isEqualTo(2),
                () -> assertThat(foundPromotionIds).contains(savedMapping1.getPromotionId()),
                () -> assertThat(foundPromotionIds).contains(savedMapping2.getPromotionId()),
                () -> assertThat(foundPromotionIds).doesNotContain(expectNotfound.getPromotionId())
        );
    }
}
