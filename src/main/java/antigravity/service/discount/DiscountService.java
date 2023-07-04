package antigravity.service.discount;

import antigravity.domain.DiscountType;
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

    public int calculateFinalDiscountPrice(int originPrice, List<Promotion> promotions) {
        int finalDiscountPrice = promotions.stream()
                .map(promotion -> productAmountDiscountFactory.createDiscountPolicy(of(promotion.getDiscountType()))
                        .applyDiscount(originPrice, promotion))
                .reduce(0, Integer::sum);
        return finalDiscountPrice + getRemainingPrice(originPrice - finalDiscountPrice);
    }

    public int getRemainingPrice(int price) {
        return price % 1000;
    }

    public int calculateFinalPrice(int originPrice, int finalDiscountPrice) {
        int priceAfterDiscount = originPrice - finalDiscountPrice;
        return normalizePrice(priceAfterDiscount);
    }

    public int normalizePrice(int price) {
        final int minPrice = 10000;
        final int maxPrice = 10000000;

        if (price < minPrice) {
            throw new BusinessException(BELOW_LOWER_LIMIT);
        } else if (price > maxPrice) {
            throw new BusinessException(EXCEEDS_UPPER_LIMIT);
        } else {
            return price;
        }
    }
}
