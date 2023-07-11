package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;

@Slf4j
@Component
public class FixDiscountedAmountCalculator implements ProductAmountDiscountCalculator {

    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        if (!validatePromotionDiscountValue(promotion)) {
            throw new BusinessException(INVALID_DISCOUNT_PARAMETER);
        }
        log.info("정액 할인 : {}원", promotion.getDiscountValue());
        return promotion.getDiscountValue();
    }

    @Override
    public boolean validatePromotionDiscountValue(Promotion promotion) {
        return promotion.getDiscountValue() > 0;
    }
}
