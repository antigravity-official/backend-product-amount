package antigravity.service;

import antigravity.domain.ProductAmount;
import antigravity.domain.ProductWithPromotions;
import antigravity.domain.entity.Product;
import antigravity.exception.InvalidDiscountPriceException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    private final PromotionReader promotionReader;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Product product = repository.getProduct(request.getProductId());

        ProductWithPromotions productWithPromotion = promotionReader.getAppliedPromotionOf(product);
        ProductAmount amount = productWithPromotion.getAmountApplied(request.getCouponIds());

        return ProductAmountResponse.builder()
                .name(amount.getName())
                .originPrice(amount.getOriginPrice())
                .discountPrice(amount.getDisplayDiscountPrice())
                .finalPrice(amount.getDisplayFinalPrice())
                .build();
    }
}
