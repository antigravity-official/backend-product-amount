package antigravity.domain.service;

import antigravity.domain.entity.Promotion;
import antigravity.exception.EntityIsInvalidException;
import antigravity.exception.EntityNotFoundException;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DiscountCalculationService {

    private final PromotionRepository repository;

    @Transactional
    public int calculateDiscountAmount(final int price, List<Integer> promotionIds) {
        final List<Promotion> promotions = repository.getPromotion(promotionIds);
        if (promotions.isEmpty()) {
            throw new EntityNotFoundException("Promotions with IDs " + promotionIds.toString() + " not found.");
        }

        final int discountPrice = applyPromotions(price, promotions);
        if (repository.updatePromotionUsedAt(promotionIds) != promotionIds.size()) {
            throw new EntityIsInvalidException("Promotions with IDs " + promotionIds.toString() + " are invalid.");
        }

        return discountPrice;
    }

    private int applyPromotions(final int price, List<Promotion> promotions) {
        return promotions.stream()
                .mapToInt(promo -> applyByPromotionType(price, promo))
                .sum();
    }

    private int applyByPromotionType(int originalPrice, Promotion promo) {
        int promoDiscountValue = promo.getDiscount_Value();

        switch(promo.getPromotion_Type()) {
            case "COUPON":
                return promoDiscountValue;
            case "CODE":
                BigDecimal floatingPrice = BigDecimal.valueOf(originalPrice)
                        .multiply(BigDecimal.valueOf(promoDiscountValue)
                        .divide(BigDecimal.valueOf(100)));
                return floatingPrice.intValue();
            default:
                throw new EntityIsInvalidException("Promotion with ID " + promo.getId() + " has invalid promotion type.");
        }
    }
}