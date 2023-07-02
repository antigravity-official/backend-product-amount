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
        LocalDate currentDate = LocalDate.now();

        List<Integer> promotionId = checkDuplicatePromotionId(couponIds);
        List<PromotionProducts> promotionProducts = promotionProductsRepository.findWithPromotionByPromotionIdIn(promotionId);

        promotionProducts.stream()
            .filter(promotion -> !isPromotionPeriod(promotion.getPromotion().getUseStartedAt(), promotion.getPromotion().getUseEndedAt(), currentDate))
            .findFirst()
            .ifPresent(promotion -> {
                throw new ProductApplicationException(
                    PromotionErrorCode.INVALID_PROMOTION_PERIOD,
                    "'" + promotion.getPromotion().getName() + "'의 " + PromotionErrorCode.INVALID_PROMOTION_PERIOD.getMessage());
            });

        List<PromotionProducts> promotion = getPromotionDiscountTypeComparator(promotionProducts);

        return promotion.stream()
                        .map(PromotionDto::from)
                        .collect(Collectors.toList());
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

    public boolean isPromotionPeriod(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    private List<PromotionProducts> getPromotionDiscountTypeComparator(List<PromotionProducts> promotionProducts) {
        return promotionProducts.stream()
            .sorted(Comparator.comparingInt(a -> a.getPromotion().getDiscountType().getPriority()))
            .collect(Collectors.toList());
    }

}
