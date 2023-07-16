package antigravity.service;

import antigravity.common.CouponFailedException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.reader.ProductReader;
import antigravity.domain.reader.PromotionReader;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductReader productReader;
    private final PromotionReader promotionReader;
    private static int MIN_PRICE = 10_000;
    private static int MAX_PRICE = 10_000_000;

    @Transactional
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        int[] couponIds = request.getCouponIds();

        Product product = productReader.getProduct(request.getProductId());
        int discountPrice = 0;
        for (int couponId : couponIds) {
            Promotion promotion = promotionReader.getPromotionByPromotionIdAndCouponId(product.getId(), couponId);
            discountPrice += calcDiscountPrice(product, promotion);
        }

        int finalPrice = product.getPrice() - discountPrice;

        finalPrice = validAndChangeFinalPrice(finalPrice);

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }

    private int validAndChangeFinalPrice(int finalPrice) {
        if (finalPrice < MIN_PRICE) {
            return MIN_PRICE;
        }

        if (finalPrice > MAX_PRICE) {
            return MAX_PRICE;
        }

        return finalPrice - (finalPrice % MIN_PRICE);
    }

    private int calcDiscountPrice(Product product, Promotion promotion) {
        Date todayDate = new Date();
        if (!todayCanUsePromotion(promotion, todayDate)) {
            throw new CouponFailedException();
        }

        if (promotion.getPromotion_type().equals(Promotion.PromotionType.COUPON)) {
            return promotion.getDiscount_value();
        }

        if (promotion.getPromotion_type().equals(Promotion.PromotionType.CODE)) {
            return (product.getPrice() * promotion.getDiscount_value()) / 100;
        }

        throw new CouponFailedException();
    }

    private boolean todayCanUsePromotion(Promotion promotion, Date todayDate) {
        return todayDate.after(promotion.getUse_started_at())
                && todayDate.before(promotion.getUse_ended_at());
    }
}
