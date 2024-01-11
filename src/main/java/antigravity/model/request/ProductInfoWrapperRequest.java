package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductInfoWrapperRequest {
    private int productId;              // 상품 ID
    private List<Integer> couponIds;    // 쿠폰 IDs (List)
}
