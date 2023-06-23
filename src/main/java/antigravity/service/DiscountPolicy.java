package antigravity.service;

import antigravity.domain.dto.PromotionDto;
import antigravity.model.response.ProductAmountResponse;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 할인 정책 적용 구현 서비스
 *
 * @author cyeji
 * @since  2023.06.22
 */
@Service
public class DiscountPolicy {

    ProductAmountResponse applyDiscountPolicy(ProductAmountResponse productAmountResponse, int originPrice, List<PromotionDto> promotionList){
        int finalPrice = originPrice;
        for(PromotionDto promotionDto : promotionList){
            finalPrice -= discountPolicyByDiscountType(finalPrice, promotionDto.getDiscountType(), promotionDto.getDiscountValue());
        }
        productAmountResponse.applyDiscountPolicy(finalPrice/1000*1000,originPrice-finalPrice);
        return  productAmountResponse;
    }

    private int discountPolicyByDiscountType(int originPrice, String promotionType, int discount){
        return switch (promotionType){
                    case "WON" -> discount;
                    case "PERCENT" -> (originPrice * discount) / 100;
                    default -> 0;
                };
    }

}
