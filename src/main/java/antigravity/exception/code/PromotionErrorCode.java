package antigravity.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PromotionErrorCode implements ErrorCode {

    INVALID_PROMOTION_PERIOD(HttpStatus.BAD_REQUEST, "프로모션 진행 기간이 아닙니다."),
    DUPLICATED_PROMOTION(HttpStatus.CONFLICT, "중복된 프로모션이 존재합니다."),
    EXCEED_ORIGIN_PRICE(HttpStatus.CONFLICT, "기존 제품가격보다 높은 할인가격의 프로모션입니다."),
    INVALID_PROMOTION_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품에 해당되는 프로모션이 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
