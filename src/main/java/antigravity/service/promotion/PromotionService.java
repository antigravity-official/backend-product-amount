package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.error.BusinessException;
import antigravity.error.ErrorCode;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static antigravity.error.ErrorCode.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionQueryRepository promotionQueryRepository;
    private final PromotionProductsQueryRepository promotionProductsQueryRepository;

    public List<Promotion> findAvailablePromotionsByProductId(int productId, List<Integer> promotionIds) {
        // 1. productId로 매핑되어 있는(사용할 수 있는) promotionIds 리스트를 반환합니다.
        List<Integer> mappingPromotionIds = findMappingIdsByProductId(productId);

        // 2. 사용하려는 프로모션이 모두 매핑되어 있다면, 요청 promotionIds에 대한 promotions를 반환합니다.
        List<Promotion> promotions = findAvailablePromotionsByIds(mappingPromotionIds);

        // 3. promotions가 유효한 promotion인지 검증하고, 유효한 promotions를 반환합니다.
        return findApplicablePromotions(promotionIds, promotions);
    }

    public List<Promotion> findPromotionsByProductId(Integer productId, List<Integer> promotionIds) {
        List<Promotion> promotionsMappedProductId = findAllPromotions(productId);
        return findApplicablePromotions(promotionIds, promotionsMappedProductId);
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
                        throw new BusinessException(NOT_APPLICABLE_SELECTED_PRODUCT);
                    }
                    return matchingPromotions.stream();
                })
                .collect(toList());
    }

    public List<Promotion> findAvailablePromotionsByIds(List<Integer> promotionIds) {
        List<Promotion> promotions = promotionQueryRepository.findPromotionsByIds(promotionIds);
        // 1. 매핑은 정상적인 프로모션이지만, 존재하지 않는 프로모션일 경우 예외를 던진다.
        // 2. 적용 기간이 도래하지 않았거나, 지난 프로모션이 요청된다면 예외를 던진다.
        if (!verifyInvalidCouponUsage(promotions, promotionIds)) {
            throw new BusinessException(NOT_EXIST_PROMOTION);
        } else if (!verifyPromotionsAreValidPeriod(promotions)) {
            throw new BusinessException(INVALID_PROMOTION_PERIOD);
        }
        return promotions;
    }

    //== 검증 메소드 ==//

    private boolean verifyInvalidCouponUsage(List<Promotion> promotions, List<Integer> promotionIds) {
        return promotions.size() == promotionIds.size();
    }

    private boolean verifyPromotionsAreValidPeriod(List<Promotion> promotions) {
        LocalDate currentDate = LocalDate.now();
        return promotions.stream()
                .allMatch(promotion -> verifyPromotionIsValidPeriod(
                        promotion.getUseStartedAt(),
                        promotion.getUseEndedAt(),
                        currentDate
                ));
    }

    private boolean verifyPromotionIsValidPeriod(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    public boolean verifyPromotionRequestsAreExists(List<Integer> promotionIds) {
        return !promotionIds.isEmpty();
    }
    //== 편의 메소드 ==//

    public List<Integer> findMappingIdsByProductId(int productId) {
        List<Integer> promotionIds = promotionProductsQueryRepository.findPromotionIdsByProductId(productId);
        if (promotionIds.isEmpty()) {
            throw new BusinessException(NO_PROMOTIONS_AVAILABLE);
        }
        return promotionIds;
    }


    private List<Promotion> findAllPromotions(int productId) {
        return findAvailablePromotionsByIds(findMappingIdsByProductId(productId));
    }
}

