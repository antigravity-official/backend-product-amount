package antigravity.domain.entity.promotion.enums;

import java.util.function.BiFunction;

public enum DiscountType {
    WON((a, b) -> b)
    , PERCENT((a, b) -> a*b/100);

    private final BiFunction<Integer, Integer, Integer> biFunction;

    DiscountType(BiFunction<Integer, Integer, Integer> biFunction) {
        this.biFunction = biFunction;
    }
    public int calculateDiscountPrice(int originalPrice, int discountValue) {
        return biFunction.apply(originalPrice, discountValue);
    }

}
