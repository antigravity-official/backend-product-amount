package antigravity.service;

import antigravity.common.CouponFailedException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.reader.ProductReader;
import antigravity.domain.reader.PromotionReader;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@DisplayName("쿠폰 적용 로직 테스트")
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductReader productReader;

    @Mock
    PromotionReader promotionReader;

    @Test
    @DisplayName("기간이 지난 쿠폰을 사용하려고 하면 오류 발생")
    void getProductAmountFailureByPromotionExpired() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1, 2})
                .build();
        when(productReader.getProduct(request.getProductId())).thenReturn(getProduct());
        when(promotionReader.getPromotionByPromotionIdAndCouponId(request.getProductId(), 1)).thenReturn(getExpiredCouponPromotion());

        assertThrows(CouponFailedException.class, () -> {
            productService.getProductAmount(request);
        });
    }

    @Test
    @DisplayName("프로모션 적용 성공 테스트 - 단일 쿠폰 : COUPON")
    void getProductAmountByCOUPON() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1})
                .build();
        Promotion promotion = getCouponPromotion();
        Product product = getProduct();
        when(productReader.getProduct(request.getProductId())).thenReturn(getProduct());
        when(promotionReader.getPromotionByPromotionIdAndCouponId(request.getProductId(), 1)).thenReturn(promotion);

        ProductAmountResponse result = productService.getProductAmount(request);

        int finalPrice = product.getPrice() - promotion.getDiscount_value();
        finalPrice = finalPrice - (finalPrice % 10_000);

        assertThat(result.getDiscountPrice()).isEqualTo(promotion.getDiscount_value());
        assertThat(result.getFinalPrice()).isEqualTo(finalPrice);
    }

    @Test
    @DisplayName("프로모션 적용 성공 테스트 - 단일 쿠폰 : CODE")
    void getProductAmountByCODE() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1})
                .build();
        Promotion promotion = getCodePromotion();
        Product product = getProduct();
        when(productReader.getProduct(request.getProductId())).thenReturn(getProduct());
        when(promotionReader.getPromotionByPromotionIdAndCouponId(request.getProductId(), 1)).thenReturn(promotion);

        ProductAmountResponse result = productService.getProductAmount(request);
        int codeDiscountValue = (product.getPrice() * promotion.getDiscount_value()) / 100;
        int finalPrice = product.getPrice() - codeDiscountValue;
        finalPrice = finalPrice - (finalPrice % 10_000);

        assertThat(result.getDiscountPrice()).isEqualTo(codeDiscountValue);
        assertThat(result.getFinalPrice()).isEqualTo(finalPrice);
    }

    @Test
    @DisplayName("프로모션 적용 성공 테스트 - 단일 쿠폰 : CODE | COUPON")
    void getProductAmountByCODEAndCoupon() {
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1, 2})
                .build();
        Promotion promotionCode = getCodePromotion();
        Promotion promotionCoupon = getCouponPromotion();
        Product product = getProduct();
        when(productReader.getProduct(request.getProductId())).thenReturn(getProduct());
        when(promotionReader.getPromotionByPromotionIdAndCouponId(request.getProductId(), 1)).thenReturn(promotionCode);
        when(promotionReader.getPromotionByPromotionIdAndCouponId(request.getProductId(), 2)).thenReturn(promotionCoupon);

        ProductAmountResponse result = productService.getProductAmount(request);
        int codeDiscountValue = (product.getPrice() * promotionCode.getDiscount_value()) / 100;
        int couponDiscountValue = promotionCoupon.getDiscount_value();

        int finalPrice = product.getPrice() - (codeDiscountValue + couponDiscountValue);
        finalPrice = finalPrice - (finalPrice % 10_000);

        assertThat(result.getDiscountPrice()).isEqualTo(codeDiscountValue + couponDiscountValue);
        assertThat(result.getFinalPrice()).isEqualTo(finalPrice);
    }

    Product getProduct() {
        return Product.builder()
                .id(1)
                .name("test")
                .price(215000)
                .build();
    }

    Promotion getExpiredCouponPromotion() {
        return Promotion.builder()
                .id(1)
                .name("test")
                .discount_type(Promotion.DiscountType.WON)
                .promotion_type(Promotion.PromotionType.COUPON)
                .discount_value(30000)
                .use_ended_at(Date.valueOf("2022-11-01"))
                .use_started_at(Date.valueOf("2023-03-01"))
                .build();
    }

    Promotion getCouponPromotion() {
        return Promotion.builder()
                .id(1)
                .name("test")
                .discount_type(Promotion.DiscountType.WON)
                .promotion_type(Promotion.PromotionType.COUPON)
                .discount_value(30000)
                .use_ended_at(Date.valueOf("2050-11-01"))
                .use_started_at(Date.valueOf("2023-03-01"))
                .build();
    }

    Promotion getCodePromotion() {
        return Promotion.builder()
                .id(1)
                .name("test code")
                .discount_type(Promotion.DiscountType.PERCENT)
                .promotion_type(Promotion.PromotionType.CODE)
                .discount_value(15)
                .use_ended_at(Date.valueOf("2050-11-01"))
                .use_started_at(Date.valueOf("2023-03-01"))
                .build();
    }
}