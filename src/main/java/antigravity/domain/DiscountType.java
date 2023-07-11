package antigravity.domain;

import java.util.Arrays;

public enum DiscountType {
    WON, PERCENT, INVALID;

    public static DiscountType of(String discountType) {
        return Arrays.stream(DiscountType.values())
                .filter(value -> value.name().equals(discountType))
                .findFirst()
                .orElse(INVALID);
    }
}
