package antigravity.service.discount;

import antigravity.error.BusinessException;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;

@Component
public class DiscountCalculatorFactory {
    public DiscountCalculator createDiscountPolicy(String discountType) {
        switch (discountType) {
            case "WON":
                return new FixDiscountCalculator();
            case "PERCENT":
                return new RateDiscountCalculator();
            default:
                throw new BusinessException(INVALID_PROMOTION_TYPE);
        }
    }
}
