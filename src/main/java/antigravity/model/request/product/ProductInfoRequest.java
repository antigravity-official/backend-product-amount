package antigravity.model.request.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductInfoRequest {

    @Positive(message = "상품 ID는 양수 값입니다.")
    @NotNull(message = "상품 ID는 필수 값입니다.")
    private int productId; //상품 ID

    private int[] couponIds; //쿠폰 ID
}
