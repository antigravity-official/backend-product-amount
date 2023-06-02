package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.error.ErrorCode;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_PERIOD;
import static antigravity.error.ErrorCode.NOT_EXIST_PROMOTION;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionQueryRepository promotionQueryRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductsQueryRepository promotionProductsQueryRepository;

    public void validatePromotions(List<Promotion> promotions) {
        LocalDate currentDate = LocalDate.now();
        promotions.stream()
                .filter(promotion -> !isCouponValid(
                        promotion.getUseStartedAt(), promotion.getUseEndedAt(), currentDate))
                .findFirst()
                .ifPresent(promotion -> {
                    throw new BusinessException(INVALID_PROMOTION_PERIOD);
                });
    }

    private boolean isCouponValid(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    public List<Promotion> findValidatePromotionsByIds(int[] promotionIds) {
        List<Promotion> promotions = promotionQueryRepository.findPromotionsByIds(promotionIds);
        if (promotions.size() != promotionIds.length) {
            throw new BusinessException(NOT_EXIST_PROMOTION);
        }
        validatePromotions(promotions);
        return promotions;
    }

    public int[] findAllPromotionIdsByProductId(int productId) {
        return promotionProductsQueryRepository.findPromotionIdsByProductId(productId);
    }

    public List<Promotion> findValidatePromotionsByProductId(int productId) {
        return findValidatePromotionsByIds(findAllPromotionIdsByProductId(productId));
    }

    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
}
