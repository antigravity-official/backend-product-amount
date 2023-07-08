package antigravity.service;

import antigravity.domain.entity.Money;
import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionService promotionService;
    private static final int PRICE_CUT_STANDARD = -3;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Product product = repository.getProduct(request.getProductId());

        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        Money originPrice = product.getPrice();
        Money discountPrice = compareWithOriginPrice(originPrice, promotionService.getDiscountPrice(product, request.getCouponIds()));
        Money finalPrice = cutPriceLessThan(getFinalPrice(originPrice, discountPrice), PRICE_CUT_STANDARD);

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice.getAmount())
                .discountPrice(discountPrice.getAmount())
                .finalPrice(finalPrice.getAmount())
                .build();
    }

    private Money compareWithOriginPrice(Money originPrice, Money discountPrice) {
        if (originPrice.isLessThan(discountPrice)) {
            return originPrice;
        }
        return discountPrice;
    }

    private Money getFinalPrice(Money originPrice, Money discountPrice) {
        return originPrice.minus(discountPrice);
    }

    private Money cutPriceLessThan(Money price, int digit) {
        return price.cutPriceLessThan(digit);
    }
}
