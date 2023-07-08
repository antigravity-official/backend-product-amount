package antigravity.service;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.Money;
import antigravity.domain.entity.Promotion;

public interface DiscountTypeService {

    public Money calculateDiscountPrice(Money originPrice, Promotion promotion);

    public boolean findType(DiscountType type);
}

