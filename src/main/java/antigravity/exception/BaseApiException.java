package antigravity.exception;

import antigravity.utils.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.support.MessageSourceAccessor;

@Getter
@Setter
public class BaseApiException extends RuntimeException {
	private Integer code;
	private String message;
	private static MessageSourceAccessor messageSource;
	private String API_RESULT_MSG_PREFIX = "api.response.msg.";

	public BaseApiException(Integer code) {
		this.code = code;
		this.message = MessageUtils.getMessage(API_RESULT_MSG_PREFIX + code);
	}

}
