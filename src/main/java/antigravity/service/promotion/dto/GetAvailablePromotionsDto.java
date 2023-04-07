package antigravity.service.promotion.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
public class GetAvailablePromotionsDto {
    private long productId;
    private List<Long> promotionIds;
    private LocalDateTime availableAt;

    public GetAvailablePromotionsDto(long productId, List<Long> promotionIds, LocalDateTime availableAt) {
        setProductId(productId);
        setPromotionIds(promotionIds);
        setAvailableAt(availableAt);
    }

    private void setProductId(long productId) {
        this.productId = productId;
    }

    private void setPromotionIds(List<Long> promotionIds) {
        if (promotionIds == null) {
            throw new IllegalArgumentException("promotionIds must not be null");
        }
        this.promotionIds = promotionIds;
    }

    private void setAvailableAt(LocalDateTime availableAt) {
        if (availableAt == null) {
            throw new IllegalArgumentException("availableAt must not be null");
        }
        this.availableAt = availableAt;
    }
}
