package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.error.BusinessException;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static antigravity.error.ErrorCode.INVALID_COUPON;
import static antigravity.error.ErrorCode.NOT_EXIST_COUPON;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionProductsRepository promotionProductsRepository;

    public void validatePromotions(List<Promotion> promotions) {
        LocalDate currentDate = LocalDate.now();
        for (Promotion promotion : promotions) {
            if (!isCouponValid(
                    promotion.getUseStartedAt(), promotion.getUseEndedAt(), currentDate)) {
                throw new BusinessException(INVALID_COUPON);
            }
        }
    }

    private boolean isCouponValid(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    public List<Promotion> findValidatePromotionsByIds(int[] promotionIds) {
        List<Promotion> promotions = promotionRepository.findPromotionsByIds(promotionIds);
        if (promotions.size() != promotionIds.length) {
            throw new BusinessException(NOT_EXIST_COUPON);
        }
        validatePromotions(promotions);
        return promotions;
    }

    public int[] findAllPromotionIdsByProductId(int productId) {
        return promotionProductsRepository.findPromotionIdsByProductId(productId);
    }

    public List<Promotion> findPromotionsByProductId(int productId) {
        return findValidatePromotionsByIds(findAllPromotionIdsByProductId(productId));
    }

    public int createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion).getId();
    }
}
