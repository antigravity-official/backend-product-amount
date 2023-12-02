package antigravity.service;

import static antigravity.constants.DiscountType.*;
import static antigravity.global.exception.ErrorMessage.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import antigravity.constants.DiscountType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.InvalidDateException;
import antigravity.exception.NotAllowedAmountRangeException;
import antigravity.exception.NotAllowedDiscountTypeException;
import antigravity.exception.NotFoundProductException;
import antigravity.exception.NotFoundPromotionException;
import antigravity.exception.NotFoundPromotionProductsException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionProductsRepository promotionProductsRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Product product = getProductById(request.getProductId());

        int originPrice = product.getPrice();
        // 프로덕트의 상품 가격 범위 확인
        validateAmountRange(product.getPrice());

        List<Integer> couponIdsList = Arrays.stream(request.getCouponIds()).boxed().toList();
        List<PromotionProducts> promotionProducts = getPromotionProducts(couponIdsList);
        // 유효 날짜 확인
        validateDate(promotionProducts);

        int discountPrice = calculateDiscountPrice(promotionProducts, originPrice);
        int finalPrice = calculateFinalPrice(originPrice, discountPrice);

        return ProductAmountResponse.from(product.getName(), originPrice, discountPrice, finalPrice);
    }

    public Product getProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new NotFoundProductException(NOT_FOUND_PRODUCT));
    }

    public List<PromotionProducts> getPromotionProducts(List<Integer> couponIds) {
        List<PromotionProducts> promotionProducts = promotionProductsRepository.findPromotionProductsByPromotionIdIn(couponIds);

        if (promotionProducts == null || promotionProducts.isEmpty() ) {
            throw new NotFoundPromotionProductsException(NOT_FOUND_PROMOTION_PRODUCTS);
        }
        return promotionProducts;
    }

    public Promotion getPromotionById(int promotionId) {
        return promotionRepository.findById(promotionId).orElseThrow(
            () -> new NotFoundPromotionException(NOT_FOUND_PROMOTION));
    }

    public void validateAmountRange(int price) {
        if (price < 10000 || price > 10000000) {
            throw new NotAllowedAmountRangeException(NOT_ALLOWED_AMOUNT_RANGE);
        }
    }

    public void validateDate(List<PromotionProducts> promotionProducts) {
        LocalDateTime now = LocalDateTime.now();

        for (PromotionProducts entity : promotionProducts) {
            Promotion promotion = getPromotionById(entity.getPromotion().getId());

            LocalDateTime startDate = promotion.getUse_started_at();
            LocalDateTime endDate = promotion.getUse_ended_at();

            if (startDate.isAfter(now) || endDate.isBefore(now)) {
                throw new InvalidDateException(INVALID_DATE);
            }
        }
    }

    public int calculateDiscountPrice(List<PromotionProducts> promotionProducts, int originPrice) {
        int discountPrice = 0;

        for (PromotionProducts entity : promotionProducts) {
            Promotion promotion = entity.getPromotion();

            DiscountType discountType = promotion.getDiscount_type();

            if (discountType.equals(WON)) {
                discountPrice += promotion.getDiscount_value();
            } else if (discountType.equals(PERCENT)) {
                discountPrice += ((originPrice * promotion.getDiscount_value()) / 100);
            } else {
                throw new NotAllowedDiscountTypeException(NOT_ALLOWED_DISCOUNT_TYPE);
            }
        }

        return discountPrice;
    }

    public int calculateFinalPrice(int originPrice, int discountPrice) {
        // 천원 단위 절삭
        int finalPrice = ((originPrice - discountPrice) / 1000) * 1000;

        // 할인 금액을 적용했을 때 음수가 되는 상황에 대한 스펙이 정해지지 않아 음수일 경우 0으로 처리한다.
        return Math.max(finalPrice, 0);
    }
}
