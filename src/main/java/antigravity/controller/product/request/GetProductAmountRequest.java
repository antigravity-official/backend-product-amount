package antigravity.controller.product.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class GetProductAmountRequest {
    @NotNull(message = "상품 고유 번호를 입력해주세요.")
    private Long productId;
    @NotNull(message = "쿠폰 고유 번호를 입력해주세요.")
    private List<Long> couponIds;
}
