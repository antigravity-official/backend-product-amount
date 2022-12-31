package antigravity.error.dto;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    INVALID_PRODUCT_PRICE(BAD_REQUEST, "상품가격 오류로 인해 판매가 불가한 상품입니다."),
    INVALID_DISCOUNT_PRICE(BAD_REQUEST, "상품 최소가격 이하로는 프로모션 적용이 불가합니다."),
    NOT_FOUND_PRODUCT(NOT_FOUND, "구매 가능한 상품정보가 존재하지 않습니다."),

    INVALID_PROMOTION_PERIOD(BAD_REQUEST,"프로모션 적용 기간이 아닙니다."),
    NOT_FOUND_PROMOTION(NOT_FOUND, "적용 가능한 프로모션이 존재하지 않습니다."),
    DUPLICATED_PROMOTION(BAD_REQUEST, "프로모션은 중복으로 적용될 수 없습니다."),
    INVALID_PROMOTION_TYPE(BAD_REQUEST, "지원하지 않는 프로모션 타입입니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
