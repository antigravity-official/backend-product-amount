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

/**
 * Domain service for calculating discounts based on promotions.
 */
@RequiredArgsConstructor
@Service
public class DiscountCalculationService {

    private final PromotionRepository repository;

    /**
     * Calculates the discount amount for a given price and list of promotion IDs.
     *
     * @param price The original price of the product.
     * @param promotionIds The IDs of promotions to apply.
     * @return The discounted price.
     * @throws EntityNotFoundException If no promotions are found for given IDs.
     * @throws EntityIsInvalidException If the number of updated promotions does not match the expected number.
     */
    @Transactional
    public int calculateDiscountAmount(final int price, List<Integer> promotionIds) {
        final List<Promotion> promotions = repository.getPromotion(promotionIds);
        if (promotions.isEmpty()) {
            throw new EntityNotFoundException("Promotions with IDs " + promotionIds.toString() + " not found.");
        }

        final int discountPrice = applyPromotions(price, promotions);
        if (repository.updatePromotionUsedAt(promotionIds) != promotionIds.size()) {
            throw new EntityIsInvalidException("Promotions with IDs " + promotionIds + " are invalid.");
        }

        return discountPrice;
    }

    /**
     * Applies promotions to the original price.
     *
     * @param price The original price of the product.
     * @param promotions The list of promotions to apply.
     * @return The price after applying promotions.
     */
    private int applyPromotions(final int price, List<Promotion> promotions) {
        return promotions.stream()
                .mapToInt(promo -> applyByPromotionType(price, promo))
                .sum();
    }

    /**
     * Applies a promotion based on its type.
     *
     * @param originalPrice The original price of the product.
     * @param promo The promotion to apply.
     * @return The discounted amount based on the promotion type.
     * @throws EntityIsInvalidException If the promotion type is invalid.
     */
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