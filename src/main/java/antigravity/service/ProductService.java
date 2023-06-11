package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorResponse;
import antigravity.enums.PromotionTypes;
import antigravity.exception.CommonException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) throws Exception {

        Product product = productRepository.getProduct(request.getProductId());

        int price = product.getPrice();
        Date nowDate = new Date();
        ArrayList<Promotion> promotions = new ArrayList<>();

        for(int couponId : request.getCouponIds()) {
            Promotion promotion = promotionRepository.getPromotion(couponId);
            if (nowDate.before(promotion.getUse_ended_at())) {
                promotions.add(promotion);
            } else {
                System.out.println("만료된 " + couponId + "번 쿠폰은 제외하고 할인이 적용됩니다.");
            }
        }

        for(int i = 0 ; i < promotions.size() ; i ++) {
            Promotion promotion = promotions.get(i);

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

        if(product.getPrice() <= price) {
            throw new CommonException(ErrorResponse.DISCOUNT_AMOUNT_OVER_ORIGIN_AMOUNT);
        }

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(product.getPrice() - price)
                .finalPrice(price)
                .build();
    }
}
