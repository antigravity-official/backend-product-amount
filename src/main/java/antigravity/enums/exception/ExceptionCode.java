package antigravity.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode { //에러 코드 정보

	//상품 관련
	PRODUCT_NOT_FOUND(-100, "해당 상품 정보가 존재하지 않습니다."),
	;

	private final int code; //에러 코드
	private final String message; //에러 메세지

}
