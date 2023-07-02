package antigravity.model.dto;

import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.entity.common.DiscountType;
import antigravity.domain.entity.common.PromotionType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromotionDto {

    private Integer id;
    private PromotionType promotionType;
    private String name;
    private DiscountType discountType;
    private int discountValue;
    private LocalDate useStartedAt;
    private LocalDate useEndedAt;

    public static PromotionDto from(PromotionProducts promotion) {
        return new PromotionDto(
            promotion.getPromotion().getId(),
            promotion.getPromotion().getPromotionType(),
            promotion.getPromotion().getName(),
            promotion.getPromotion().getDiscountType(),
            promotion.getPromotion().getDiscountValue(),
            promotion.getPromotion().getUseStartedAt(),
            promotion.getPromotion().getUseEndedAt()
        );
    }
    
}
