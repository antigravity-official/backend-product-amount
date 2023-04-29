package antigravity.domain.entity.promotion_products;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionProducts {
    private int id;
    private int promotionId;
    private int productId;
}
