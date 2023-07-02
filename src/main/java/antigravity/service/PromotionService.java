package antigravity.service;

import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.PromotionErrorCode;
import antigravity.model.dto.PromotionDto;
import antigravity.repository.PromotionProductsRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionProductsRepository promotionProductsRepository;

    @Transactional(readOnly = true)
    public List<PromotionDto> getPromotion(int[] couponIds) {
        List<Integer> promotionId = checkDuplicatePromotionId(couponIds);
        List<PromotionProducts> promotionProducts = promotionProductsRepository.findWithPromotionByPromotionIdIn(promotionId);

        isExistPromotion(couponIds.length, promotionProducts.size());
        isPromotionPeriod(promotionProducts);

        List<PromotionProducts> promotion = getPromotionDiscountTypeComparator(promotionProducts);

        return promotion.stream()
                        .map(PromotionDto::from)
                        .collect(Collectors.toList());
    }

    private void isExistPromotion(int ouponIdsLength, int promotionSize) {
        if ((ouponIdsLength != promotionSize) || (promotionSize == 0)) {
            throw new ProductApplicationException(PromotionErrorCode.NOT_EXIST_PROMOTION);
        }
    }

    public void isPromotionPeriod(List<PromotionProducts> promotionProducts) {
        LocalDate currentDate = LocalDate.now();

        promotionProducts.stream()
            .filter(promotion -> !currentDate.isAfter(promotion.getPromotion().getUseStartedAt()) || !currentDate.isBefore(promotion.getPromotion().getUseEndedAt()))
            .findFirst()
            .ifPresent(promotion -> {
                throw new ProductApplicationException(
                    PromotionErrorCode.INVALID_PROMOTION_PERIOD,
                    "'" + promotion.getPromotion().getName() + "'의 " + PromotionErrorCode.INVALID_PROMOTION_PERIOD.getMessage());
            });
    }

    private List<Integer> checkDuplicatePromotionId(int[] couponIds) {
        List<Integer> promotionId = Arrays.stream(couponIds)
            .boxed()
            .distinct()
            .collect(Collectors.toList());

        if (couponIds.length != promotionId.size()) {
            throw new ProductApplicationException(PromotionErrorCode.DUPLICATED_PROMOTION);
        }
        return promotionId;
    }

    private List<PromotionProducts> getPromotionDiscountTypeComparator(List<PromotionProducts> promotionProducts) {
        return promotionProducts.stream()
            .sorted(Comparator.comparingInt(a -> a.getPromotion().getDiscountType().getPriority()))
            .collect(Collectors.toList());
    }

}
