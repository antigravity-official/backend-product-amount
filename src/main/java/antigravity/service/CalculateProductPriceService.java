package antigravity.service;

import antigravity.domain.entity.common.DiscountType;
import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.model.dto.ProductDto;
import antigravity.model.dto.PromotionDto;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CalculateProductPriceService {

    private final ProductService productService;
    private final PromotionService promotionService;

    @Transactional(readOnly = true)
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        ProductDto product = productService.getProduct(request.getProductId());
        List<PromotionDto> promotion = promotionService.getPromotion(request.getCouponIds());

        // TODO : product - promotion

        String name = product.getName();
        int originPrice = product.getPrice();

        int discountPrice = getCalculateDiscountPrice(promotion, originPrice);

        int totalPrice = originPrice - discountPrice;
        validProductLimitPrice(totalPrice);

        return ProductAmountResponse.builder()
            .name(name)
            .originPrice(originPrice)
            .discountPrice(discountPrice)
            .finalPrice(totalPrice)
            .build();
    }

    private int getCalculateDiscountPrice(List<PromotionDto> promotion, int originPrice) {
        int discountSum = 0;

        for (PromotionDto dto : promotion) {
            DiscountType discountType = dto.getDiscountType();

            int discountValue = dto.getDiscountValue();

            discountSum += discountType.applyDiscount(originPrice - discountSum, discountValue, discountType);
        }

        return discountSum += getRemainingPrice(originPrice - discountSum);
    }

    public int getRemainingPrice(int price) {
        return price % 1000;
    }

    public void validProductLimitPrice(int price) {
        final int minPrice = 10000;
        final int maxPrice = 10000000;

        if (price < minPrice) {
            throw new ProductApplicationException(ProductErrorCode.MIN_PRICE_LIMIT);
        } else if (price > maxPrice) {
            throw new ProductApplicationException(ProductErrorCode.MAX_PRICE_LIMIT);
        }
    }
}
