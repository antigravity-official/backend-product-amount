package antigravity.error;

public enum ErrorCode {
    INVALID_COUPON_FOR_PRODUCT(1001, "해당 상품에 적용할 수 없는 쿠폰입니다."),
    COUPON_NOT_YET_AVAILABLE(1002, "해당 쿠폰은 아직 사용할 수 없습니다."),
    COUPON_EXPIRED(1003, "해당 쿠폰은 기간이 만료되었습니다.");

    private int code;
    private String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}