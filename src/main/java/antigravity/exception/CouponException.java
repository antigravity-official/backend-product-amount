package antigravity.exception;

import antigravity.error.ErrorCode;

public class CouponException extends Exception {
    private ErrorCode errorCode;
    private String couponName;

    public CouponException(ErrorCode errorCode, String couponName) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.couponName = couponName;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCouponName() {
        return couponName;
    }
}