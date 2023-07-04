package antigravity.service.discount;

import antigravity.domain.DiscountType;
import antigravity.error.BusinessException;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;

@Component
public class ProductAmountDiscountFactory {
    public ProductAmountDiscountCalculator createDiscountPolicy(DiscountType discountType) {
        switch (discountType) {
            case WON:
                return new FixProductAmountDiscountCalculator();
            case PERCENT:
                return new RateProductAmountDiscountCalculator();
            default:
                throw new BusinessException(INVALID_PROMOTION_TYPE);
        }
    }
}
