package antigravity.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500001, "INTERNAL_SERVER_ERROR", "알 수 없는 서버 에러가 발생하였습니다."),

    PROMOTION_TYPE_NOT_FOUND(400101, "PROMOTION_TYPE_NOT_FOUND", "존재하지 않는 프로모션 타입입니다."),
    NOT_SUPPORTED_PROMOTION(400102, "NOT_SUPPORTED_PROMOTION", "해당 상품에 지원되지 않는 프로모션입니다."),

    INVALID_DISCOUNT_PRICE(400201, "INVALID_DISCOUNT_PRICE", "유효하지 않은 할인 금액입니다."),
    PRODUCT_NOT_FOUND(400202, "PRODUCT_NOT_FOUND", "존재하지 않는 상품입니다."),
    ;

    private final int status;
    private final String code;
    private final String defaultMessage;
}
