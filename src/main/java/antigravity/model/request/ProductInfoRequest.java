package antigravity.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@Getter
public class ProductInfoRequest {
    private int productId;
    private int[] couponIds;
}
