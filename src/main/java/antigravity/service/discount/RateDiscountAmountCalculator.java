package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;

@Slf4j
@Component
public class RateDiscountAmountCalculator implements ProductAmountDiscountCalculator {

    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        if(isDiscountAmountValid(promotion)){
            throw new BusinessException(INVALID_DISCOUNT_PARAMETER);
        }
        log.info("정률 할인 : {}%", promotion.getDiscountValue());
        return originPrice / 100 * promotion.getDiscountValue();
    }

    /**
     * @param promotion - 정률할인에서 할인 비율은 0 < DiscountValue <= 100
     * @return
     */
    @Override
    public boolean isDiscountAmountValid(Promotion promotion) {
        return promotion.getDiscountValue() > 100 || promotion.getDiscountValue() <= 0;
    }
}
