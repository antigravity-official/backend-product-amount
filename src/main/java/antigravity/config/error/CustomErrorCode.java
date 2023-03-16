package antigravity.config.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {

    // Response : 3000
    FAIL_TO_USE_PROMOTION(3000, HttpStatus.OK, "해당 쿠폰을 사용할 수 없습니다."),
    INVALID_PROMOTION_EXPIRATION(3001, HttpStatus.OK, "해당 쿠폰의 유효기간이 만료되었습니다."),
    INVALID_PROMOTION_BEFORE_DATE(3002, HttpStatus.OK, "쿠폰 사용 기간이 아닙니다."),
    INVALID_PROMOTION_PRICE(3002, HttpStatus.OK, "할인 금액이 상품 금액보다 큽니다."),

    // Server error : 4000
    INTERNAL_SERVER_ERROR(4000, HttpStatus.INTERNAL_SERVER_ERROR, "서버와의 연결에 실패했습니다."),
    DATABASE_ERROR(4001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 연결에 실패했습니다.")
    ;

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
