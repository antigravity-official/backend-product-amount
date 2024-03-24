package antigravity.domain.entity.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static antigravity.domain.entity.promotion.enums.DiscountType.PERCENT;
import static antigravity.domain.entity.promotion.enums.DiscountType.WON;
import static antigravity.domain.entity.promotion.enums.PromotionType.CODE;
import static antigravity.domain.entity.promotion.enums.PromotionType.COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PromotionTest {

    @DisplayName("프로모션 시작일에 프로모션 사용이 가능하다.")
    @Test
    void isAvailableOnTest1() {
        //given
        Promotion januaryCoupon = Promotion.builder()
                .name("새해맞이 1월 한정 1000원 할인 쿠폰")
                .promotionType(COUPON)
                .discountType(WON)
                .discountValue(1_000)
                .useStartedAt(LocalDate.of(2024, 01, 01))
                .useEndedAt((LocalDate.of(2024, 01, 31)))
                .build();
        LocalDate firstDateOfJanuary = LocalDate.of(2024, 01, 01);

        //when
        boolean result = januaryCoupon.isAvailableOn(firstDateOfJanuary);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("프로모션 종료일에 프로모션 사용이 가능하다.")
    @Test
    void isAvailableOnTest2() {
        //given
        Promotion januaryCoupon = Promotion.builder()
                .name("새해맞이 1월 한정 1000원 할인 쿠폰")
                .promotionType(COUPON)
                .discountType(WON)
                .discountValue(1_000)
                .useStartedAt(LocalDate.of(2024, 01, 01))
                .useEndedAt((LocalDate.of(2024, 01, 31)))
                .build();
        LocalDate lastDateOfJanuary = LocalDate.of(2024, 1, 31);

        //when
        boolean result = januaryCoupon.isAvailableOn(lastDateOfJanuary);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("프로모션 종료 후에는 프로모션을 사용할 수 없다.")
    @Test
    void isAvailableOnTest3() {
        //given
        Promotion januaryCoupon = Promotion.builder()
                .name("새해맞이 1월 한정 1000원 할인 쿠폰")
                .promotionType(COUPON)
                .discountType(WON)
                .discountValue(1_000)
                .useStartedAt(LocalDate.of(2024, 01, 01))
                .useEndedAt((LocalDate.of(2024, 01, 31)))
                .build();
        LocalDate firstDateOfFebruary = LocalDate.of(2024, 2, 1);

        //when  //then
        assertThatThrownBy(() -> januaryCoupon.isAvailableOn(firstDateOfFebruary))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("쿠폰에서 할인금액 조회 시 할인금액을 그대로 응답한다.")
    @Test
    void getDiscountFromTest1() {
        //given
        Promotion promotion = Promotion.builder()
                .promotionType(COUPON)
                .discountType(WON)
                .discountValue(1_000)
                .build();

        //when
        int discountPrice= promotion.getDiscountFrom(10_000);

        //then
        assertThat(discountPrice).isEqualTo(1_000);
    }

    @DisplayName("코드에서 할인금액 조회 시 상품의 금액에서 비율로 할인된 금액을 응답한다.")
    @Test
    void getDiscountFromTest2() {
        //given
        Promotion promotion = Promotion.builder()
                .promotionType(CODE)
                .discountType(PERCENT)
                .discountValue(50)
                .build();

        //when
        int discountPrice= promotion.getDiscountFrom(10_000);

        //then
        assertThat(discountPrice).isEqualTo(5_000);

    }

}