package antigravity.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 상품 테이블에 존재하지 않는 상품을 조회할 경우 예외를 던집니다.
    NOT_EXIST_PRODUCT("P001", "존재하지 않는 상품 입니다", 404),

    // 쿠폰 테이블에 존재하지 않는 쿠폰이 매핑되어 사용하려고 할 경우 예외를 던집니다.
    NOT_EXIST_PROMOTION("P002", "존재하지 않는 쿠폰 입니다", 404),

    // 상품에 매핑된 쿠폰, 할인코드 중 단 한 개라도 유효하지 않다면(유효기간 이슈) 예외를 던집니다.
    INVALID_PROMOTION_PERIOD("P003", "쿠폰의 유효기간을 확인해주세요.", 400),

    // 지문에 제시된 `WON`, `PERCENT`를 제외한 다른 프로모션 타입을 가진 쿠폰이 매핑되어 사용하려고 할 경우 예외를 던집니다.
    INVALID_PROMOTION_TYPE("P004", "지원하지 않는 쿠폰입니다.", 400),

    // 최종 가격(할인가 및 쿠폰 미적용가)이 상한 초과, 하한 미만이라면 예외를 던집니다.
    EXCEEDS_UPPER_LIMIT("P005", "최종 상품 가격이 상한 초과입니다. (10,000,000 KRW)", 400),
    BELOW_LOWER_LIMIT("P006", "최종 상품 가격이 하한 미만입니다. (10,000 KRW)", 400);

    private final String code;
    private final String message;
    private final int status;
}
