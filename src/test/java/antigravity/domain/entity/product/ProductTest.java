package antigravity.domain.entity.product;

import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static antigravity.domain.entity.promotion.enums.DiscountType.WON;
import static antigravity.domain.entity.promotion.enums.PromotionType.COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProductTest {
    @Spy
    private Product spyProduct;

    @DisplayName("상품에 적용 가능한 프로모션 조회 시 정상적으로 discountPrice가 계산된다.")
    @Test
    void applyPromotionsTest1() {
        //given
        Promotion januaryPromotion1 = createJanuaryPromotionWithNameAndValue("january_1000", 1000);
        Promotion januaryPromotion2 = createJanuaryPromotionWithNameAndValue("january_2000", 2000);
        Promotion januaryPromotion3 = createJanuaryPromotionWithNameAndValue("january_3000", 3000);
        List<Promotion> promotions = List.of(januaryPromotion1, januaryPromotion2, januaryPromotion3);

        BDDMockito.given(spyProduct.getPromotionProducts())
                .willReturn(createPromotionProducts(spyProduct, promotions));

        LocalDate requestDate = LocalDate.of(2024,01,31);

        //when
        int discountPrice = spyProduct.getDiscountsByPromotions(promotions, requestDate);

        //then
        assertThat(discountPrice).isEqualTo(6_000);
    }

    @DisplayName("날짜가 지난 프로모션은 적용할 수 없다.")
    @Test
    void applyPromotionsTest2() {
        //given
        Promotion januaryPromotion1 = createJanuaryPromotionWithNameAndValue("january_1000", 1000);
        List<Promotion> promotions = List.of(januaryPromotion1);

        LocalDate requestDate = LocalDate.of(2024,02,01);

        //when  //then
        assertThatThrownBy(() -> spyProduct.getDiscountsByPromotions(promotions, requestDate))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("적용 대상이 아닌 프로모션을 적용할 수 없다.")
    @Test
    void applyPromotionsTest3() {
        //given
        Promotion januaryPromotion1 = createJanuaryPromotionWithNameAndValue("january_1000", 1000);
        Promotion januaryPromotion2 = createJanuaryPromotionWithNameAndValue("january_2000", 2000);

        BDDMockito.given(spyProduct.getPromotionProducts())
                .willReturn(createPromotionProducts(spyProduct, List.of(januaryPromotion1)));

        LocalDate requestDate = LocalDate.of(2024,01,31);

        //when  //then
        assertThatThrownBy(() -> spyProduct.getDiscountsByPromotions(List.of(januaryPromotion2), requestDate))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("할인 금액을 받아 확정 금액을 결정한다.")
    @Test
    void finalizePriceTest() {
        //given
        Product product = Product.builder().price(100_000).build();
        int discountPrice = 100;

        //when
        product.finalizePrice(discountPrice);

        //then
        assertThat(product.getFinalPrice()).isEqualTo(99_000);
    }

    private static Promotion createJanuaryPromotionWithNameAndValue(String name, int discountValue) {
        return Promotion.builder()
                .name(name)
                .promotionType(COUPON)
                .discountType(WON)
                .discountValue(discountValue)
                .useStartedAt(LocalDate.of(2024, 01, 01))
                .useEndedAt(LocalDate.of(2024, 01, 31))
                .build();
    }

    private PromotionProducts createPromotionProduct(Product product, Promotion promotion1) {
        return PromotionProducts.builder()
                .product(product)
                .promotion(promotion1)
                .build();
    }

    private List<PromotionProducts> createPromotionProducts(Product product, List<Promotion> promotions) {
        List<PromotionProducts> promotionProducts = new ArrayList<>();
        for(Promotion p : promotions) {
            promotionProducts.add(createPromotionProduct(product, p));
        }
        return promotionProducts;
    }

}