package antigravity.domain;

public enum DiscountType {
    WON, PERCENT, INVALID;

    public static DiscountType of(String discountType) {
        for (DiscountType value : values()) {
            if (value.name().equals(discountType))
                return value;
        }
        return INVALID;
    }
}
