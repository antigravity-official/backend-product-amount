package antigravity.service;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.Money;
import antigravity.domain.entity.Promotion;
import org.springframework.stereotype.Service;

@Service
public class WonDiscountTypeService implements DiscountTypeService {

    @Override
    public Money calculateDiscountPrice(Money originPrice, Promotion promotion) {
        return promotion.getDiscountValue();
    }

    @Override
    public boolean findType(DiscountType type) {
        return DiscountType.WON == type;
    }
}
