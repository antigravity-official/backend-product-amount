package antigravity.common;

public class CouponFailedException extends RuntimeException{
    public CouponFailedException() {
        super("적용할 수 없는 쿠폰입니다.");
    }
}
