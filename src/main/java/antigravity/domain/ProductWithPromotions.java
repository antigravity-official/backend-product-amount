package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotSupportedPromotionException;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ProductWithPromotions {
    private Product product;

    @Builder.Default
    private List<Promotion> promotions = new ArrayList<>();

    public ProductAmount getAmountApplied(int[] couponIds) {
        canApply(couponIds);

        List<Integer> discountedPrices = getDiscountedPrices(couponIds);

        ProductAmount amount = new ProductAmount(product);
        discountedPrices.forEach(amount::discount);
        return amount;
    }

    private void canApply(int[] couponIds) {
        List<Integer> promotionIds = promotions.stream().map(Promotion::getId).toList();
        for (int couponId : couponIds) {
            if (!promotionIds.contains(couponId)) {
                throw new NotSupportedPromotionException(couponId);
            }
        }
    }

    private List<Integer> getDiscountedPrices(int[] couponIds) {
        List<Integer> discountedPrices = new ArrayList<>();
        for (int couponId : couponIds) {
            promotions.stream().filter(p -> p.getId() == couponId)
                    .findAny()
                    .ifPresent(promotion -> discountedPrices.add(PriceCalculator.apply(product, promotion)));
        }
        return discountedPrices;
    }
}
