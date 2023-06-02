package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.discount.DiscountService;
import antigravity.service.promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductPriceService {

    private final PromotionService promotionService;
    private final DiscountService discountService;
    private final ProductService productService;

    public ProductAmountResponse getProductAmount(int productId) {
        Product product = productService.findProductByProductId(productId);
        List<Promotion> promotions = promotionService.findValidatePromotionsByProductId(productId);
        return calculateProductAmount(product, promotions);
    }

    public ProductAmountResponse calculateProductAmount(Product product, List<Promotion> promotions) {
        int originPrice = product.getPrice();
        int finalDiscountPrice = discountService.calculateFinalDiscountPrice(originPrice, promotions);
        int finalPrice = discountService.calculateFinalPrice(originPrice, finalDiscountPrice);
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountPrice(finalDiscountPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
