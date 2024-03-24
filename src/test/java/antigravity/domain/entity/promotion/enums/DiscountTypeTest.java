package antigravity.domain.entity.promotion.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTypeTest {

    @DisplayName("DiscountType이 WON이면 할인 금액만 응답한다.")
    @Test
    void calculateTest1() {
        //given
        DiscountType won = DiscountType.WON;
        int originalPrice = 10_000;
        int discountValue = 1_000;

        //when
        int result = won.calculateDiscountPrice(originalPrice, discountValue);

        //then
        assertThat(result).isEqualTo(1_000);
    }

    @DisplayName("DiscountType이 PERCENT이면 기본 금액에서 비율만큼 할인된 금액을 응답한다..")
    @Test
    void calculateTest2() {
        //given
        DiscountType percent = DiscountType.PERCENT;
        int originalPrice = 10_000;
        int discountValue = 20;

        //when
        int result = percent.calculateDiscountPrice(originalPrice, discountValue);

        //then
        assertThat(result).isEqualTo(2_000);
    }

}