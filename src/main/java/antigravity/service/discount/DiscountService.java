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

    private final ProductAmountDiscountFactory productAmountDiscountFactory;

    /**
     * @param originPrice   - 상품 정상 판매가
     * @param promotions    - 적용해야 하는 프로모션 List
     * @return              - 최종 할인 판매 가격
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
     * @param originPrice           - 상품 정상 판매가
     * @param finalDiscountValue     - 할인이 적용되어야 하는 값
     * @return                      - 최소 최대 검증 이후, 할인 판매가
     */
    public int getFinalDiscountedPrice(int originPrice, int finalDiscountValue) {
        int discountedPrice = originPrice - finalDiscountValue;
        return normalizePrice(discountedPrice);
    }

    /**
     * @param price         - 절삭 전 가격
     * @return              - truncateValue 만큼 절삭 후 가격
     */
    private int getRemainingPrice(int price) {
        final int truncateValue = 1000;
        return price % truncateValue;
    }

    /**
     * @param discountedPrice                 - 최종 할인 적용가
     * @return                                - 최소/최대 검증 이후, 할인 적용가
     */
    private int normalizePrice(int discountedPrice) {
        final int minPrice = 10000;
        final int maxPrice = 10000000;

        if (discountedPrice < minPrice) {
            throw new BusinessException(BELOW_LOWER_LIMIT);
        } else if (discountedPrice > maxPrice) {
            throw new BusinessException(EXCEEDS_UPPER_LIMIT);
        } else {
            return discountedPrice;
        }
    }
}
