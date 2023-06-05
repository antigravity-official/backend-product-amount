package antigravity.service;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static antigravity.domain.entity.DiscountType.PERCENT;
import static antigravity.domain.entity.DiscountType.WON;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CalculateDiscountService {
    public Integer getDiscountValue(PromotionProducts promotionProducts) {
        DiscountType discountType = promotionProducts.getPromotion().getPromotionInfo().getDiscount_type();
        Double wonOrPercentDiscountValue = promotionProducts.getPromotion().getPromotionInfo().getDiscount_value();
        Integer discountValue = 0;

        if (discountType == WON) {
            discountValue = wonOrPercentDiscountValue.intValue();
        }

        if (discountType == PERCENT) {
            Integer originalPrice = promotionProducts.getProduct().getPrice();
            discountValue = (int) (originalPrice * wonOrPercentDiscountValue);
        }

        return discountValue;
    }
}
