package antigravity.domain.dto;

import antigravity.domain.enums.DiscountType;
import antigravity.domain.enums.PromotionType;
import java.util.Date;
import lombok.Data;

@Data
public class PromotionDto {

    private int id; // 쿠폰 고유식별자
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name; // 쿠폰 이름
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private Date useStartedAt; // 쿠폰 사용가능 시작 기간
    private Date useEndedAt; // 쿠폰 사용가능 종료 기간

    public static PromotionDto from(PromotionProductsDto promotionProductsDto) {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.id = promotionProductsDto.getPromotionId();
        promotionDto.promotionType = promotionProductsDto.getPromotionType();
        promotionDto.name = promotionProductsDto.getPromotionName();
        promotionDto.discountType = promotionProductsDto.getDiscountType();
        promotionDto.discountValue = promotionProductsDto.getDiscountValue();
        promotionDto.useStartedAt = promotionProductsDto.getPromotionUseStartedAt();
        promotionDto.useEndedAt = promotionProductsDto.getPromotionUseEndedAt();
        return promotionDto;
    }
}
