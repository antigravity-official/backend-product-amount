package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.enums.PromotionTypes;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        Product product = productRepository.getProduct(request.getProductId());
        int price = product.getPrice();

        for(int couponId : request.getCouponIds()) {

            Promotion promotion = promotionRepository.getPromotion(couponId);

            String promotionName = promotion.getPromotion_type();
            int discountValue = promotion.getDiscount_value();

            PromotionTypes types = PromotionTypes.valueOf(promotionName);
            switch (types) {
                case COUPON:
                    price -= discountValue;
                    break;
                case CODE:
                    price -= (price * discountValue / 100);
                    break;
                default:
            }
        }

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(product.getPrice() - price)
                .finalPrice(price)
                .build();
    }
}
