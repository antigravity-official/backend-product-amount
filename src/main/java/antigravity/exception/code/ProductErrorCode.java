package antigravity.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    // 상품 테이블에 존재하지 않는 상품을 조회할 경우
    NOT_EXIST_PRODUCT(HttpStatus.NOT_FOUND, "존재하지 않는 상품 입니다"),
    MIN_PRICE_LIMIT(HttpStatus.BAD_REQUEST, "최종 상품 가격이 상한 초과입니다. (최대 상품 가격 : 10,000,000)"),
    MAX_PRICE_LIMIT(HttpStatus.BAD_REQUEST, "최종 상품 가격이 하한 미만입니다. (최소 상품 가격 : 10,000)"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
