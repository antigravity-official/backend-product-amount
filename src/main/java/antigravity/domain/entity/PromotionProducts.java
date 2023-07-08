package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotionProducts {
    private Integer id;
    private Integer promotionId;
    private Integer productId;
}
