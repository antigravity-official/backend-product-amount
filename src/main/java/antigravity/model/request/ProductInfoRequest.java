package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductInfoRequest {
    private long productId;
    private List<Long> couponIds;
}
