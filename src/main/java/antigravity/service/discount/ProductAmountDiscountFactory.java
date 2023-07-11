package antigravity.service.discount;

import antigravity.domain.DiscountType;
import antigravity.error.BusinessException;
import antigravity.service.discount.discounted_amount.FixDiscountedAmountService;
import antigravity.service.discount.discounted_amount.DiscountedAmountUtil;
import antigravity.service.discount.discounted_amount.RateDiscountedAmountService;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;

@Component
public class ProductAmountDiscountFactory {

    public DiscountedAmountUtil calculateDiscountedAmount(DiscountType discountType) {
        switch (discountType) {
            case WON:
                return new FixDiscountedAmountService();
            case PERCENT:
                return new RateDiscountedAmountService();
            default:
                throw new BusinessException(INVALID_PROMOTION_TYPE);
        }
    }
}
