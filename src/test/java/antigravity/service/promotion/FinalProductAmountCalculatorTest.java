package antigravity.service.promotion;

import antigravity.domain.product.Product;
import antigravity.domain.promotion.DiscountType;
import antigravity.domain.promotion.Promotion;
import antigravity.domain.promotion.PromotionType;
import antigravity.service.product.FinalProductAmountCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static antigravity.domain.product.ProductFixture.aProduct;
import static antigravity.domain.promotion.PromotionFixture.aPromotion;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinalProductAmountCalculatorTest {
    FinalProductAmountCalculator finalProductAmountCalculator = new FinalProductAmountCalculator();

    @Test
    @DisplayName("200,000원 상품에 100,000원 정액 쿠폰을 프로모션 적용할 수 있을 경우 200,000원을 반환한다.")
    void getPromotionsAppliedPriceTest_1() {
        // given
        Product product = aProduct()
                .price(200_000)
                .build();
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(PromotionType.COUPON)
                        .discountType(DiscountType.WON)
                        .discountValue(100_000)
                        .build()
        );

        // when
        long result = finalProductAmountCalculator.getPromotionsAppliedPrice(product, promotions);

        // then
        assertEquals(100_000, result);
    }

    @Test
    @DisplayName("적용 가능한 프로모션이 존재하지 않을 경우 기존 가격을 그대로 반환한다.")
    void getPromotionsAppliedPriceTest_2() {
        // given
        Product product = aProduct()
                .price(200_000)
                .build();
        List<Promotion> promotions = asList();

        // when
        long result = finalProductAmountCalculator.getPromotionsAppliedPrice(product, promotions);

        // then
        assertEquals(200_000, result);
    }

    @Test
    @DisplayName("적용 가능한 프로모션이 존재하지 않을 경우 천단위는 절삭되지 않는다.")
    void getPromotionsAppliedPriceTest_3() {
        // given
        Product product = aProduct()
                .price(232_000)
                .build();
        List<Promotion> promotions = asList();

        // when
        long result = finalProductAmountCalculator.getPromotionsAppliedPrice(product, promotions);

        // then
        assertEquals(232_000, result);
    }

    @Test
    @DisplayName("200,000원 상품에 100,000원 정액 쿠폰과 20% 정률 쿠폰을 적용할 수 있을 경우 80,000원을 반환한다.")
    void getPromotionsAppliedPriceTest_4() {
        // given
        Product product = aProduct()
                .price(200_000)
                .build();
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(PromotionType.COUPON)
                        .discountType(DiscountType.WON)
                        .discountValue(100_000)
                        .build(),
                aPromotion()
                        .promotionType(PromotionType.COUPON)
                        .discountType(DiscountType.PERCENT)
                        .discountValue(20)
                        .build()
        );

        // when
        long result = finalProductAmountCalculator.getPromotionsAppliedPrice(product, promotions);

        // then
        assertEquals(80_000, result);
    }

    @Test
    @DisplayName(
        "적용가능한 프로모션이 존재하고 최종 결과에 천단위가 포함되어 있다면 천단위는 절삭한다." +
        "232,000원에 적용가능한 100,000원 프로모션과 20% 프로모션이 존재한다면" +
        "모든 프로모션을 적용한 후 최종적인 금액은 105,600원인데 프로모션이 적용이 되었기 때문에 천단위는 절삭되어 100,000원을 반환한다."
    )
    void getPromotionsAppliedPriceTest_5() {
        // given
        Product product = aProduct()
                .price(232_000)
                .build();
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(PromotionType.COUPON)
                        .discountType(DiscountType.WON)
                        .discountValue(100_000)
                        .build(),
                aPromotion()
                        .promotionType(PromotionType.COUPON)
                        .discountType(DiscountType.PERCENT)
                        .discountValue(20)
                        .build()
        );

        // when
        long result = finalProductAmountCalculator.getPromotionsAppliedPrice(product, promotions);

        // then
        assertEquals(100_000, result);
    }
}
