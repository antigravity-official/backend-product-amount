package antigravity.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR("C001", HttpStatus.INTERNAL_SERVER_ERROR, "500 내부 서버 오류입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

}
