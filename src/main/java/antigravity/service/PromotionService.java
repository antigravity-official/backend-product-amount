package antigravity.service;

import antigravity.domain.entity.Money;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository repository;
    private final DiscountTypeFactory factory;

    public Money getDiscountPrice(Product product, List<Integer> couponIds) {
        List<Promotion> promotions = repository.getPromotionBy(product.getId(), couponIds);

        Money totalDiscountPrice = new Money(BigDecimal.ZERO);
        for (Promotion promotion : promotions) {
            DiscountTypeService discountType = factory.get(promotion.getDiscountType());
            Money discountPrice = discountType.calculateDiscountPrice(product.getPrice(), promotion);
            totalDiscountPrice = totalDiscountPrice.plus(discountPrice);
        }

        return totalDiscountPrice;
    }
}
