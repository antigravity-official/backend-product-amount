package antigravity.domain.dto;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 * 제품 프로모션 객체
 * 해당 객체는 제품과 연관관계가 이루어져있는 프로모션을 매핑해 조회하는 용도로 사용되는 객체입니다.
 * @author cyeji
 * @since 2023.06.22
 */
@Data
@Builder
public class PromotionProductsDto {

    private int productId;
    private String productName;
    private int promotionId;
    private String promotionName;
    private int price;
    private String promotionType;
    private String discountType;
    private int discountValue;
    private Date promotionUseStartedAt;
    private Date promotionUseEndedAt;

}
