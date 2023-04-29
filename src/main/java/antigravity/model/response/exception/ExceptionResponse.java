package antigravity.model.response.exception;

import antigravity.enums.exception.ExceptionCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {

	private int code; //에러 코드

	private String message; //에러 메세지

	public static ExceptionResponse of(ExceptionCode code) {
		return ExceptionResponse.builder()
			.code(code.getCode())
			.message(code.getMessage()).build();
	}

}
