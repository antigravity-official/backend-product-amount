package antigravity.service;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

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

        Date now = new Date();


        if (discountType == WON && !isExpired(promotionProducts, now)) {
            discountValue = wonOrPercentDiscountValue.intValue();
        }

        if (discountType == PERCENT && !isExpired(promotionProducts, now)) {
            Integer originalPrice = promotionProducts.getProduct().getPrice();
            discountValue = (int) (originalPrice * wonOrPercentDiscountValue);
        }

        return discountValue;
    }

    private static Boolean isExpired(PromotionProducts promotionProducts, Date now) {
        if (promotionProducts.getPromotion().getUse_ended_at() == null) {
            return false;
        } else {
            return now.after(promotionProducts.getPromotion().getUse_ended_at());
        }
    }
}
