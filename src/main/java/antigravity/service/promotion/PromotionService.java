package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static antigravity.error.ErrorCode.*;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionVerfiyService promotionVerfiyService;
    private final PromotionQueryRepository promotionQueryRepository;
    private final PromotionProductsQueryRepository promotionProductsQueryRepository;

    public List<Promotion> getValidMappedPromotionsByProductId(int productId, List<Integer> promotionIds) {
        // 1. productId로 매핑되어 있는(사용할 수 있는) promotionIds 리스트를 반환합니다.
        List<Integer> mappingPromotionIds = findMappingIdsByProductId(productId);

        // 2. 사용하려는 프로모션이 모두 매핑되어 있다면, 요청 promotionIds에 대한 유효한 promotions를 반환합니다.
        List<Promotion> promotions = findValidPromotionsByIds(mappingPromotionIds);

        // 3. promotions가 유효한 promotion인지 검증하고, 유효한 promotions를 반환합니다.
        return findApplicablePromotions(promotionIds, promotions);
    }

    public List<Integer> findMappingIdsByProductId(int productId) {
        List<Integer> promotionIds = promotionProductsQueryRepository.findPromotionIdsByProductId(productId);
        if (promotionIds.isEmpty()) {
            throw new BusinessException(NO_PROMOTIONS_AVAILABLE);
        }
        return promotionIds;
    }

    public List<Promotion> findValidPromotionsByIds(List<Integer> promotionIds) {
        List<Promotion> promotions = promotionQueryRepository.findPromotionsByIds(promotionIds);
        // 1. 매핑은 정상적인 프로모션이지만, 존재하지 않는 프로모션일 경우 예외를 던진다.
        // 2. 적용 기간이 도래하지 않았거나, 지난 프로모션이 요청된다면 예외를 던진다.
        if (promotions.size() != promotionIds.size()) {
            throw new BusinessException(NOT_EXIST_PROMOTION);
        } else if (!promotionVerfiyService.isEachPromotionHasValidExpirationDate(promotions)) {
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
                        throw new BusinessException(NOT_APPLICABLE_SELECTED_PRODUCT);
                    }
                    return matchingPromotions.stream();
                })
                .collect(toList());
    }
}

