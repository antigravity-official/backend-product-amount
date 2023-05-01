package antigravity.model.response.exception;

import antigravity.enums.exception.ExceptionInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ExceptionResponse {

	@Schema(description = "에러 코드", example = "-100")
	private int code;

	@Schema(description = "에러 메세지", example = "올바른 요청이 아닙니다.")
	private String message;

	public static ExceptionResponse of(final ExceptionInfo code) {
		return ExceptionResponse.builder()
			.code(code.getCode())
			.message(code.getMessage()).build();
	}

	public static ExceptionResponse of(final int code, final String message) {
		return ExceptionResponse.builder()
			.code(code)
			.message(message).build();
	}

}
