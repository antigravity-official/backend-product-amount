package antigravity.service.discount;

import antigravity.domain.Promotion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RateDiscountCalculator implements DiscountCalculator {
    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        log.info("정률 할인 : {}%", promotion.getDiscountValue());
        return originPrice / 100 * promotion.getDiscountValue();
    }
}
