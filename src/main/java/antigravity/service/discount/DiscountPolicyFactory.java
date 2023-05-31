package antigravity.service.discount;

import antigravity.error.BusinessException;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_PROMOTION;

@Component
public class DiscountPolicyFactory {

    public DiscountPolicy createDiscountPolicy(String discountType) {
        if ("WON".equals(discountType)) {
            return new FixDiscountPolicy();
        } else if ("PERCENT".equals(discountType)) {
            return new RateDiscountPolicy();
        } else {
            throw new BusinessException(INVALID_PROMOTION);
        }
    }
}
