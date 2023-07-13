package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static antigravity.error.ErrorCode.*;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionQueryRepository promotionQueryRepository;
    private final PromotionProductsQueryRepository promotionProductsQueryRepository;

    public List<Integer> findMappedPromotionIdsByProductId(int productId) {
        List<Integer> promotionIds = promotionProductsQueryRepository.findPromotionIdsByProductId(productId);
        if (promotionIds.isEmpty()) {
            throw new BusinessException(NO_PROMOTIONS_AVAILABLE);
        }
        return promotionIds;
    }

    public List<Promotion> findAllPromotionsByIds(List<Integer> promotionIds) {
        List<Promotion> promotions = promotionQueryRepository.findPromotionsByIds(promotionIds);
        // 적용 기간이 도래하지 않았거나, 지난 프로모션이 요청된다면 예외를 던진다.
        if (!isEachPromotionHasValidExpirationDate(promotions)) {
            throw new BusinessException(INVALID_PROMOTION_PERIOD);
        }
        return promotions;
    }

    /**
     * @param desiredPromotionIds - Product에 적용하고자 하는 promotionIds
     * @param availablePromotions - ProductPromotions Table에 해당 ProductId로 매핑되어있는 promotions
     */
    public List<Promotion> findApplicablePromotions(List<Integer> desiredPromotionIds, List<Promotion> availablePromotions) {
        return desiredPromotionIds.stream()
                .flatMap(desiredId -> {
                    List<Promotion> matchingPromotions = availablePromotions.stream()
                            .filter(promotion -> promotion.getId() == desiredId)
                            .collect(toList());
                    if (matchingPromotions.isEmpty()) {
                        throw new BusinessException(NOT_AVAILABLE_PROMOTION);
                    }
                    return matchingPromotions.stream();
                })
                .collect(toList());
    }

    //== 검증 메소드 ==//

    public void verifyPromotionRequestExistence(List<Integer> promotionIds) {
        if (!isPromotionRequestExistence(promotionIds)) {
            throw new BusinessException(NO_REQUEST_PROMOTIONS);
        }
    }

    private boolean isEachPromotionHasValidExpirationDate(List<Promotion> promotions) {
        LocalDate currentDate = LocalDate.now();
        return promotions.stream()
                .allMatch(promotion -> isPromotionHasValidExpirationDate(
                        promotion.getUseStartedAt(),
                        promotion.getUseEndedAt(),
                        currentDate
                ));
    }

    private boolean isPromotionHasValidExpirationDate(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    private boolean isPromotionRequestExistence(List<Integer> promotionIds) {
        return !promotionIds.isEmpty();
    }
}

