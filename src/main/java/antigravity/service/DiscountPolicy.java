package antigravity.service;

import static antigravity.domain.enums.PromotionType.CODE;

import antigravity.domain.dto.PromotionDto;
import antigravity.domain.enums.PromotionType;
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
            finalPrice -= discountPolicyByPromotionType(finalPrice, promotionDto.getPromotionType(), promotionDto.getDiscountValue());
        }
        productAmountResponse.applyDiscountPolicy(finalPrice/1000*1000,originPrice-finalPrice);
        return  productAmountResponse;
    }

    private int discountPolicyByPromotionType(int originPrice, PromotionType promotionType, int discount){
        if(promotionType == CODE) return originPrice * discount / 100;
        return discount;
    }

}
