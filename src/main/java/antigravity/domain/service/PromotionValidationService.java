package antigravity.domain.service;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.EntityNotFoundException;
import antigravity.repository.PromotionProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PromotionValidationService {

    private final PromotionRepository promotionRepository;
    private final PromotionProductRepository promotionProductRepository;

    @Transactional(readOnly = true)
    public Boolean validatePromotion(final int productId, List<Integer> promotionIds) {
        List<PromotionProducts> validPromotionProducts = promotionProductRepository.getPromotionProduct(productId);
        if (!checkPromotionValidity(promotionIds, extractIds(validPromotionProducts))) {
            throw new EntityNotFoundException("Promotions with IDs " + promotionIds.toString() +
                    " invalid for Product with ID " + productId + ".");
        }

        return true;
    }

    private Boolean checkPromotionValidity(List<Integer> expected, List<Integer> actual) {
        Set<Integer> expectedSet = new HashSet<>(expected);

        return (expectedSet.size() == actual.size() &&
                expected.containsAll(actual) &&
                checkPromotionExpiration(actual));
    }

    private Boolean checkPromotionExpiration(List<Integer> ids) {
        List<Promotion> promotions = promotionRepository.getPromotion(ids);
        LocalDate now = LocalDate.now();
        Boolean arePromotionsValid = promotions.stream()
                .allMatch(promo -> promo.getUse_Started_At().isBefore(now) &&
                                   promo.getUse_Ended_At().isAfter(now));

        return arePromotionsValid;
    }

    private List<Integer> extractIds(List<PromotionProducts> promotionProducts) {
        return promotionProducts.stream()
                .map(PromotionProducts::getPromotionId)
                .collect(Collectors.toList());
    }
}
