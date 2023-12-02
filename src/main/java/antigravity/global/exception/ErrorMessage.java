package antigravity.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorMessage {

	NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당 정보로 등록된 제품이 존재하지 않습니다."),
	NOT_FOUND_PROMOTION(HttpStatus.NOT_FOUND, "해당 정보로 등록된 프로모션이 존재하지 않습니다."),
	NOT_FOUND_PROMOTION_PRODUCTS(HttpStatus.NOT_FOUND, "해당 정보로 등록된 매칭 정보가 존재하지 않습니다"),

	NOT_ALLOWED_AMOUNT_RANGE(HttpStatus.BAD_REQUEST, "가격 범위가 잘못 설정되어 관리자의 검토가 필요한 상품입니다."),
	INVALID_DATE(HttpStatus.BAD_REQUEST, "할인 적용 가능 시간을 다시 확인해 주세요."),

	NOT_ALLOWED_DISCOUNT_TYPE(HttpStatus.BAD_REQUEST, "타입 정보가 일치하지 않습니다.");
	;

	private HttpStatus status;
	private String message;

	ErrorMessage(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
