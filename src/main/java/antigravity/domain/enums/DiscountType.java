package antigravity.domain.enums;

public enum DiscountType {
    WON, PERCENT;

    public static DiscountType of(String discountType) {
        for (DiscountType value : values()) {
            if(value.name().equals(discountType)) {
                return value;
            }
        }
        return WON;
    }
}
