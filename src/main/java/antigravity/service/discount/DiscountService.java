package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
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

    /**
     * @param originPrice - 상품 정상 판매가
     * @param promotions  - 적용해야 하는 프로모션 List
     * @return - 최종 할인 판매 가격
     */
    public int getFinalDiscountedAmount(int originPrice, List<Promotion> promotions) {
        int discountedPrice = promotions.stream()
                .map(promotion -> productAmountDiscountFactory.calculateDiscountedAmount(
                                of(promotion.getDiscountType()))
                        .applyDiscount(originPrice, promotion))
                .reduce(0, Integer::sum);
        return discountedPrice + getRemainingPrice(originPrice - discountedPrice);
    }

    /**
     * @param originPrice        - 상품 정상 판매가
     * @param finalDiscountedAmount  - 할인이 적용되어야 하는 값
     * @return - 계층값 이상 확인 이후 최종 할인 가격 리턴
     */
    public int getFinalDiscountedPrice(int originPrice, int finalDiscountedAmount) {
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

    private boolean hasLowerValueThanLowerBound(int finalPrice) {
        return finalPrice < LOWER_BOUND;
    }

    private boolean hasHigherValueThanUpperBound(int finalPrice) {
        return finalPrice > UPPER_BOUND;
    }
}
