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

    private int productId; // 제품 식별자 
    private String productName; // 제품 이름 
    private int promotionId; // 프로모션 식별자 
    private String promotionName; // 프로모션 명 
    private int price; // 제품 가격 
    private String promotionType; // 프로모션 유형 
    private String discountType; // 할인 유형 
    private int discountValue; // 할인 가격 
    private Date promotionUseStartedAt; // 프로모션 시작일 
    private Date promotionUseEndedAt; // 프로모션 종료일 

}
