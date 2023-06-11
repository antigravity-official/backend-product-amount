package antigravity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum ErrorResponse {
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),

    PRODUCT_ID_NOT_FOUND(404,"PRODUCT-ERR-404","해당 id의 PRODUCT 를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(404,"PRODUCT-ERR-404","PRODUCT 조회중 오류가 발생했습니다."),

    PROMOTION_ID_NOT_FOUND(404,"PROMOTION-ERR-404","해당 id의 PROMOTION 을 찾을 수 없습니다."),
    PROMOTION_NOT_FOUND(404,"PROMOTION-ERR-404","PROMOTION 조회중 오류가 발생했습니다."),

    EXPIRED_DATE_COUPON(402, "COUPON-ERR-402", "COUPON 의 날짜가 만료되었습니다."),

    DISCOUNT_AMOUNT_OVER_ORIGIN_AMOUNT(402, "DISCOUNT-ERR-402", "할인 금액이 원래 금액보다 큽니다."),

    MIN_PRODUCT_PRICE(402, "DISCOUNT-ERR-402", "상품 금액이 최소 금액 10000 보다 작습니다."),
    MAX_PRODUCT_PRICE(402, "DISCOUNT-ERR-402", "상품 금액이 최대 금액 10000000 보다 큽니다."),

    ;

    private int status;
    private String code;
    private String message;
}
