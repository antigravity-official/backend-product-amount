package antigravity.service;

import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.PromotionProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {
    private final PromotionProductRepository promotionProductRepository;
    private final CalculateDiscountService calculateDiscountService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Integer[] couponIds = getCouponIds(request);

        List<PromotionProducts> promotionProducts = promotionProductRepository.fetch(couponIds, request.getProductId());

        Integer discountSum = getDiscountSum(promotionProducts);

        validateDiscountSum(promotionProducts, discountSum);

        return ProductAmountResponse.from(
                promotionProducts.get(0).getProduct(),
                discountSum
        );
    }

    private static Integer[] getCouponIds(ProductInfoRequest request) {
        Integer[] couponIds = new Integer[request.getCouponIds().length];

        for (int i = 0; i < request.getCouponIds().length; i++) {
            couponIds[i] = request.getCouponIds()[i];
        }
        return couponIds;
    }

    private Integer getDiscountSum(List<PromotionProducts> promotionProducts) {
        Integer discountSum = 0;
        for (PromotionProducts promotionProduct : promotionProducts) {
            discountSum += calculateDiscountService.getDiscountValue(promotionProduct);
        }
        return discountSum;
    }

    private static void validateDiscountSum(List<PromotionProducts> promotionProducts, Integer discountSum) {
        if (promotionProducts.get(0).getProduct().getPrice() < discountSum) throw new RuntimeException();
    }
}
