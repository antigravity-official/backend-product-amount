package antigravity.domain.entity;

import lombok.Builder;

@Builder
public class PromotionProducts {
    private Integer id;
    private Integer promotionId;
    private Integer productId;
}
