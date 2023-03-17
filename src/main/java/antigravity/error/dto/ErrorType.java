package antigravity.error.dto;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    INVALID_PRODUCT_PRICE(BAD_REQUEST, "상품가격 오류 상품입니다."),
    INVALID_DISCOUNT_PRICE(BAD_REQUEST, "상품의 최소가격 이하로는 프로모션 적용이 불가능 합니다."),
    NOT_FOUND_PRODUCT(NOT_FOUND, "구매 가능한 상품정보가 존재하지 않습니다."),

    INVALID_PROMOTION_PERIOD_BEFORE(BAD_REQUEST,"해당 프로모션의 사용가능 기간 이전입니다. 프로모션 정보를 확인해주세요."),
    INVALID_PROMOTION_PERIOD_EXPIRED(BAD_REQUEST,"해당 프로모션의 사용가능 기간이 만료되었습니다."),
    DUPLICATED_PROMOTION(BAD_REQUEST, "같은 프로모션은 중복으로 적용될 수 없습니다."),
    NOT_FOUND_PROMOTION(NOT_FOUND, "프로모션 중 적용가능한 것이 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
