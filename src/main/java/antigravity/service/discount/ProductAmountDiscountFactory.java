package antigravity.service.discount;

import antigravity.domain.DiscountType;
import antigravity.error.BusinessException;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;

@Component
public class ProductAmountDiscountFactory {

    public ProductAmountDiscountCalculator calculateDiscountedAmount(DiscountType discountType) {
        switch (discountType) {
            case WON:
                return new FixDiscountedAmountCalculator();
            case PERCENT:
                return new RateDiscountAmountCalculator();
            default:
                throw new BusinessException(INVALID_PROMOTION_TYPE);
        }
    }
}
