package antigravity.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 조회하려는 상품이, 상품 테이블에 존재하지 않는다면 예외를 던진다.
    NOT_EXIST_PRODUCT("P001", "존재하지 않는 상품입니다", 404),

    // 해당 상품에 사용할 수 없는 프로모션(상품에 매핑되어 있지 않은)이라면 예외를 던진다.
    // 매핑은 이루어졌으나, 해당 매핑이 존재하지 않는 프로모션을 참조하고, 그 프로모션이 요청되면 예외를 던진다.
    NOT_AVAILABLE_PROMOTION("P002", "해당 상품에 사용할 수 없는 프로모션이 요청되었습니다.", 404),

    // 상품에 매핑된 단 한 개라도 유효기간이 도래하지 않았거나 만료되었다면 예외를 던진다.
    INVALID_PROMOTION_PERIOD("P003", "요청 프로모션의 유효기간을 확인해주세요.", 400),

    // `WON`, `PERCENT`를 제외한 다른 프로모션 타입이 매핑되어 사용하려고 할 경우 예외를 던진다.
    INVALID_PROMOTION_TYPE("P004", "지원하지 않는 프로모션입니다.", 400),

    // 최종 가격(할인가 및 프로모션 적용가)이 상한 초과라면 예외를 던진다.
    EXCEEDS_UPPER_LIMIT("P005", "최종 상품 가격이 상한 초과입니다. (10,000,000 KRW)", 400),

    // 최종 가격(할인가 및 프로모션 적용가)이 하한 미만이면 예외를 던진다.
    BELOW_LOWER_LIMIT("P006", "최종 상품 가격이 하한 미만입니다. (10,000 KRW)", 400),

    // 정률할인 프로모션의 인자가 0 < Percent < 100 이 아닐경우 예외를 던진다.
    // 정액할인 프로모션의 인자가 0 > Percent 라면 예외를 던집니다.
    INVALID_DISCOUNT_PARAMETER("P007", "DiscoutValue가 정상적이지 않은 할인 쿠폰입니다.", 400),

    // 조회하려는 상품에, 어떠한 프로모션의 적용도 되어있지 않은 경우, 예외를 던진다.
    NO_PROMOTIONS_AVAILABLE("P008", "해당 상품은 어떠한 프로모션도 적용할 수 없습니다.", 400),

    // 조회하려는 상품에, 어떠한 프로모션의 적용도 되어있지 않은 경우, 예외를 던진다.
    NO_REQUEST_PROMOTIONS("P009", "어떠한 프로모션도 요청되지 않았습니다.", 400);

    private final String code;
    private final String message;
    private final int status;
}
