package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductInfoRequest {
    private int productId;    // 상품 ID
    private int[] couponIds;  // 쿠폰 IDs (Array)
}
