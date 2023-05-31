package antigravity.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_EXIST_PRODUCT("P001", "존재하지 않는 상품 입니다", 404),
    NOT_EXIST_COUPON("P002", "존재하지 않는 쿠폰 입니다", 404),
    INVALID_COUPON("P003", "쿠폰의 유효기간을 확인해주세요.", 400),
    INVALID_PROMOTION("P004", "지원하지 않는 쿠폰입니다.", 400),
    EXCEEDS_UPPER_LIMIT("P005", "최종 상품 가격이 상한 초과입니다. (10,000,000 KRW)", 400),
    BELOW_LOWER_LIMIT("P006", "최종 상품 가격이 하한 미만입니다. (10,000 KRW)", 400);

    private final String code;
    private final String message;
    private final int status;
}
