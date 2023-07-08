package antigravity.model.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductInfoRequest {
    private Integer productId;
    private List<Integer> couponIds;
}
