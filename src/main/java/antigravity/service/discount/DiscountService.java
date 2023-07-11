package antigravity.service.discount;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.error.BusinessException;
import antigravity.service.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static antigravity.domain.DiscountType.of;
import static antigravity.error.ErrorCode.BELOW_LOWER_LIMIT;
import static antigravity.error.ErrorCode.EXCEEDS_UPPER_LIMIT;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private static final int LOWER_BOUND = 10000;
    private static final int UPPER_BOUND = 10000000;
    private static final int TRUNCATE_VALUE = 1000;

    private final ProductAmountDiscountFactory productAmountDiscountFactory;

    public ProductAmountResponse applyDiscount(Product product, List<Promotion> promotions) {
        final int originPrice = product.getPrice();
        final int finalDiscountedAmount = getFinalDiscountedAmount(originPrice, promotions);
        final int finalDiscountedPrice = getFinalDiscountedPrice(originPrice, finalDiscountedAmount);
        return ProductAmountResponse.of(
                product.getName(),
                originPrice,
                finalDiscountedAmount,
                finalDiscountedPrice
        );
    }

    private int getFinalDiscountedAmount(int originPrice, List<Promotion> promotions) {
        int discountedPrice = promotions.stream()
                .map(promotion -> productAmountDiscountFactory.calculateDiscountedAmount(
                                of(promotion.getDiscountType()))
                        .getDiscountedValue(originPrice, promotion))
                .reduce(0, Integer::sum);
        return discountedPrice + getRemainingPrice(originPrice - discountedPrice);
    }


    private int getFinalDiscountedPrice(int originPrice, int finalDiscountedAmount) {
        return checkInvalidRangeOfBounds(originPrice - finalDiscountedAmount);
    }


    private int getRemainingPrice(int price) {
        return price % TRUNCATE_VALUE;
    }


    private int checkInvalidRangeOfBounds(int discountedPrice) {
        if (hasLowerValueThanLowerBound(discountedPrice)) {
            throw new BusinessException(BELOW_LOWER_LIMIT);
        } else if (hasHigherValueThanUpperBound(discountedPrice)) {
            throw new BusinessException(EXCEEDS_UPPER_LIMIT);
        } else {
            return discountedPrice;
        }
    }



    //== 검증 메소드 ==//

    private boolean hasLowerValueThanLowerBound(int finalPrice) {
        return finalPrice < LOWER_BOUND;
    }


    private boolean hasHigherValueThanUpperBound(int finalPrice) {
        return finalPrice > UPPER_BOUND;
    }
}
