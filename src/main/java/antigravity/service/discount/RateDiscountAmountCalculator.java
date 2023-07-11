package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;

@Slf4j
@Component
public class RateDiscountAmountCalculator implements ProductAmountDiscountCalculator {

    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        if(!validatePromotionDiscountValue(promotion)){
            throw new BusinessException(INVALID_DISCOUNT_PARAMETER);
        }
        log.info("정률 할인 : {}%", promotion.getDiscountValue());
        return originPrice / 100 * promotion.getDiscountValue();
    }

    @Override
    public boolean validatePromotionDiscountValue(Promotion promotion) {
        return promotion.getDiscountValue() > 0 && promotion.getDiscountValue() <= 100;
    }
}
