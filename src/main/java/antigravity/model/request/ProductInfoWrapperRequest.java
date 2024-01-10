package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductInfoWrapperRequest {
    private int productId;
    private List<Integer> couponIds;
}
