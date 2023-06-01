package antigravity.service.discount;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.error.BusinessException;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static antigravity.error.ErrorCode.BELOW_LOWER_LIMIT;
import static antigravity.error.ErrorCode.EXCEEDS_UPPER_LIMIT;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountPolicyFactory discountPolicyFactory;

    public ProductAmountResponse calculateProductAmount(Product product, List<Promotion> promotions) {
        int originPrice = product.getPrice();
        int finalDiscountPrice = calculateFinalDiscountPrice(originPrice, promotions);
        int finalPrice = calculateFinalPrice(originPrice, finalDiscountPrice);
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountPrice(finalDiscountPrice)
                .finalPrice(finalPrice)
                .build();
    }

    private int calculateFinalDiscountPrice(int originPrice, List<Promotion> promotions) {
        int finalDiscountPrice = 0;
        for (Promotion promotion : promotions) {
            DiscountPolicy discountPolicy = discountPolicyFactory.createDiscountPolicy(promotion.getDiscountType());
            finalDiscountPrice += discountPolicy.applyDiscount(originPrice, promotion);
        }
        return finalDiscountPrice + getRemainingPrice(originPrice - finalDiscountPrice);
    }

    private int getRemainingPrice(int price) {
        return price % 1000;
    }

    private int calculateFinalPrice(int originPrice, int finalDiscountPrice) {
        int priceAfterDiscount = originPrice - finalDiscountPrice;
        return normalizePrice(priceAfterDiscount);
    }

    private int normalizePrice(int price) {
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
