package antigravity.service.discount;

import antigravity.error.BusinessException;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;

@Component
public class DiscountPolicyFactory {
    public DiscountPolicy createDiscountPolicy(String discountType) {
        switch (discountType) {
            case "WON":
                return new FixDiscountPolicy();
            case "PERCENT":
                return new RateDiscountPolicy();
            default:
                throw new BusinessException(INVALID_PROMOTION_TYPE);
        }
    }
}
