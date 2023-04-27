package antigravity.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ExceptionResponse {
    private String message; // exception 메시지
    public ExceptionResponse(String message) {
        this.message = message;
    }
}
