package antigravity.domain.service;

import antigravity.domain.entity.Product;
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

    private BigDecimal applyDiscount(int price, Promotion promo) {
        BigDecimal originalPrice = BigDecimal.valueOf(price);
        BigDecimal promoDiscountValue = new BigDecimal(promo.getDiscount_Value());

        switch(promo.getPromotion_Type()) {
            case "COUPON":
                return promoDiscountValue;
            case "CODE":
                return originalPrice.multiply(promoDiscountValue
                        .divide(BigDecimal.valueOf(100)));
            default:
                throw new EntityIsInvalidException("Promotion with ID " + promo.getId() + " has invalid promotion type.");
        }
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateDiscountAmount(final int price, List<Integer> promotionIds) {
        List<Promotion> promotions = repository.getPromotion(promotionIds);
        if (promotions.isEmpty()) {
            throw new EntityNotFoundException("Promotions with IDs " + promotionIds.toString() + " not found.");
        }

        return promotions.stream()
                .map(promo -> applyDiscount(price, promo))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}