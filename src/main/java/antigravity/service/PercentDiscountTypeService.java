package antigravity.service;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.Money;
import antigravity.domain.entity.Promotion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PercentDiscountTypeService implements DiscountTypeService {

    @Override
    public Money calculateDiscountPrice(Money originPrice, Promotion promotion) {
        return originPrice.multiply(promotion.getDiscountValue())
                .divide(new Money(new BigDecimal(100)));
    }

    @Override
    public boolean findType(DiscountType type) {
        return DiscountType.PERCENT == type;
    }
}
