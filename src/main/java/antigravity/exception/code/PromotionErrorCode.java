package antigravity.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PromotionErrorCode implements ErrorCode {

    // 프로모션이 존재하지 않는 상품을 조회할 경우
    NOT_EXIST_PROMOTION(HttpStatus.NOT_FOUND, "존재하지 않는 프로모션 입니다."),
    EXPIRED_PROMOTION_PERIOD(HttpStatus.BAD_REQUEST, "기한이 지난 프로모션 입니다."),
    DUPLICATED_PROMOTION(HttpStatus.CONFLICT, "중복된 프로모션이 존재합니다."),
    EXCEED_ORIGIN_PRICE(HttpStatus.CONFLICT, "기존 제품가격보다 높은 할인가격의 프로모션입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
