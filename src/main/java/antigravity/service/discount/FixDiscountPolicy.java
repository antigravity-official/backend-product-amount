package antigravity.service.discount;

import antigravity.domain.entity.Promotion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FixDiscountPolicy implements DiscountPolicy {
    @Override
    public int applyDiscount(int originPrice, Promotion promotion) {
        log.info("정액 할인 : {}원", promotion.getDiscountValue());
        return promotion.getDiscountValue();
    }
}


