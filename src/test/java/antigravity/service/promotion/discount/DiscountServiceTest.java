package antigravity.service.promotion.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static antigravity.error.ErrorCode.BELOW_LOWER_LIMIT;
import static antigravity.global.PromotionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[SpringBootTest] DiscountService Test")
class DiscountServiceTest extends ServiceTestSupport {

    @Autowired
    private DiscountService discountService;

    private List<Promotion> onlyFixPromotions;
    private List<Promotion> onlyRatePromotions;
    private List<Promotion> mixPromotions;

    @BeforeEach
    void setUp(){
        onlyFixPromotions = new ArrayList<>(Arrays.asList(VALID_PROMOTION1.toEntity(), VALID_PROMOTION4.toEntity())); // 총 83,000원 정액 할인
        onlyRatePromotions = new ArrayList<>(Arrays.asList(VALID_PROMOTION2.toEntity(), VALID_PROMOTION3.toEntity())); // 총 83,000원 정액 할인
        mixPromotions = new ArrayList<>(Arrays.asList(VALID_PROMOTION1.toEntity(), VALID_PROMOTION2.toEntity())); // 총 83,000원 정액 할인
    }

    /**
     * 예외 테스트 1
     * 최초 판매가격 : 35,000원
     * 할인 정책 : 27,000원 정액 할인
     * 최종 판매가가 10,000원 이하이므로 예외를 던진다.
     */
    @Test
    @DisplayName("[Discount] - 할인 서비스 예외 테스트 1 - 할인 적용 후 상품 가격이 하한가 이하로 떨어지면 예외를 던진다")
    void belowLowerLimitPrice() {
        // given - @BeforeEach setUp
        // when & then
        assertThatThrownBy(() -> discountService.calculateFinalPrice(35000, 27000))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(BELOW_LOWER_LIMIT.getMessage());
    }

    /**
     * 정상 성공 TestCase 1
     * 최초 판매가격 : 464,960원
     * 할인 정책 : 83,000원 정액 할인
     * - 절삭 전 금액 : 464,960 - 83,000 = 381,960원
     * - 절삭 금액 : 960원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 83,000 + 960 = 83,960원
     */
    @Test
    @DisplayName("[Discount] - 할인 서비스 정상 테스트 1 - 정액 할인 2개를 정상적으로 실행한다")
    void validDiscountTest1() {
        // given - @BeforeEach setUp
        // when & then
        assertThat(discountService.calculateFinalDiscountPrice(464960, onlyFixPromotions)).isEqualTo(83960);
    }

    /**
     * 정상 성공 TestCase 2
     * 최초 판매가격 : 379,379원
     * 할인 정책 : 15% + 35% 정률 할인
     * - 절삭 전 금액 : 189,690원
     * - 절삭 금액 : 689원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 189,690 + 689 = 190,379원
     */
    @Test
    @DisplayName("[Discount] - 할인 서비스 정상 테스트 2 - 정률 할인 2개를 정상적으로 실행한다")
    void validDiscountTest2() {
        // given - @BeforeEach setUp
        // when & then
        assertThat(discountService.calculateFinalDiscountPrice(379999, onlyRatePromotions)).isEqualTo(189999);
    }

    /**
     * 정상 성공 TestCase 3
     * 최초 판매가격 : 158,500원
     * 할인 정책 : (1) 최초 판매가 기준 15% 정률 + (2) 30,000원 정액 할인
     * - 정률 할인 15% -> 23,775원 할인
     * - 정액 할인 30,000원
     * - 프로모션 총 할인 53,775원
     * - 절삭 전 금액 : 158,500 - 53,775 = 104,725원
     * - 절삭 금액 : 725원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 53,775 + 725 = 54,500원
     * - 최종 금액 = 158,500 - 54,500 = 104,000원
     */
    @Test
    @DisplayName("[Discount] - 할인 서비스 정상 테스트 2 - 정률 + 정액 혼합 할인 2개를 정상적으로 실행한다")
    void validDiscountTest3() {
        // given - @BeforeEach setUp
        // when & then
        assertThat(discountService.calculateFinalDiscountPrice(158500, mixPromotions)).isEqualTo(54500);
    }

    @Test
    @DisplayName("[Discount] - 할인 서비스 정상 테스트 3 - 1,000원 단위 절삭 가격을 정상적으로 계산한다")
    void validRemainingPrice() {
        // given & when & then
        assertAll(
                () -> assertThat(discountService.getRemainingPrice(5800)).isEqualTo(800),
                () -> assertThat(discountService.getRemainingPrice(369369)).isEqualTo(369),
                () -> assertThat(discountService.getRemainingPrice(6220)).isEqualTo(220),
                () -> assertThat(discountService.getRemainingPrice(699669)).isEqualTo(669)
        );
    }
}
