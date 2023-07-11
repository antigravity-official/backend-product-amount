package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;

@Slf4j
@Component
public class FixDiscountedAmountCalculator implements ProductAmountDiscountCalculator {

    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        if (!isDiscountAmountValid(promotion)) {
            throw new BusinessException(INVALID_DISCOUNT_PARAMETER);
        }
        log.info("정액 할인 : {}원", promotion.getDiscountValue());
        return promotion.getDiscountValue();
    }

    /**
     * @param promotion - 정액할인에서 할인 금액은 0 <= DiscountValue [0 && positive int]
     * @return
     */
    @Override
    public boolean isDiscountAmountValid(Promotion promotion) {
        return promotion.getDiscountValue() >= 0;
    }
}
