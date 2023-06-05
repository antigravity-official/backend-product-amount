package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionInfo;
import antigravity.domain.entity.PromotionProducts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static antigravity.domain.entity.DiscountType.PERCENT;
import static antigravity.domain.entity.DiscountType.WON;
import static antigravity.domain.entity.PromotionType.CODE;
import static antigravity.domain.entity.PromotionType.COUPON;

public class CalculateDiscountServiceTest {
    @Test
    @DisplayName("%할인")
    public void testGetDiscountPercent() {
        PromotionProducts promotionProducts = new PromotionProducts();
        Promotion promotion = new Promotion();

        PromotionInfo promotionInfo = PromotionInfo.of(
                CODE,
                PERCENT,
                0.2
        );
        Product product = new Product();

        promotion.setPromotionInfo(promotionInfo);
        promotionProducts.setPromotion(promotion);
        promotionProducts.setProduct(product);
        product.setPrice(10000);

        CalculateDiscountService calculateDiscountService = new CalculateDiscountService();
        Integer discount = calculateDiscountService.getDiscountValue(promotionProducts);
        Assertions.assertEquals(2000, discount);
    }

    @Test
    @DisplayName("금액 할인")
    public void testGetDiscountAmount() {
        PromotionProducts promotionProducts = new PromotionProducts();
        Promotion promotion = new Promotion();

        PromotionInfo promotionInfo = PromotionInfo.of(
                COUPON,
                WON,
                1000d
        );
        Product product = new Product();

        promotion.setPromotionInfo(promotionInfo);
        promotionProducts.setPromotion(promotion);
        promotionProducts.setProduct(product);
        product.setPrice(10000);

        CalculateDiscountService calculateDiscountService = new CalculateDiscountService();
        Integer discount = calculateDiscountService.getDiscountValue(promotionProducts);
        Assertions.assertEquals(1000, discount);
    }

    @Test
    @DisplayName("만료된 프로모션")
    public void testGetDiscountExpired() throws ParseException {
        PromotionProducts promotionProducts = new PromotionProducts();
        Promotion promotion = new Promotion();

        PromotionInfo promotionInfo = PromotionInfo.of(
                COUPON,
                WON,
                1000d
        );
        Product product = new Product();

        promotion.setPromotionInfo(promotionInfo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2023-05-04");
        promotion.setUse_ended_at(date);
        promotionProducts.setPromotion(promotion);
        promotionProducts.setProduct(product);
        product.setPrice(10000);

        CalculateDiscountService calculateDiscountService = new CalculateDiscountService();
        Integer discount = calculateDiscountService.getDiscountValue(promotionProducts);
        Assertions.assertEquals(0, discount);
    }
}