package antigravity.repository;

import antigravity.domain.entity.promotion.Promotion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PromotionRepositoryTest {

    @Autowired
    private PromotionRepository promotionRepository;

    @DisplayName("int 배열로 프로모션 id를 받아 DB 조회 후 List<Promotion>를 반환한다.")
    @Test
    void findAllByIdIn() {
        //given
        Promotion promotion1 = createSamplePromotionNamed("sample promotion 1");
        Promotion promotion2 = createSamplePromotionNamed("sample promotion 2");
        Promotion promotion3 = createSamplePromotionNamed("sample promotion 3");
        promotionRepository.saveAll(List.of(promotion1, promotion2, promotion3));

        //when
        List<Promotion> allByIdArray = promotionRepository.findAllByIdIn(new int[]{promotion1.getId(), promotion2.getId()});

        //then
        assertThat(allByIdArray).hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder("sample promotion 1", "sample promotion 2");

    }

    private static Promotion createSamplePromotionNamed(String name) {
        return Promotion.builder()
                .name(name)
                .build();
    }

}