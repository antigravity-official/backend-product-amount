package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.entity.common.DiscountType;
import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.exception.code.PromotionErrorCode;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionProductsRepository promotionProductsRepository;

    @Transactional(readOnly = true)
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        int productId = request.getProductId();
        int[] couponIds = request.getCouponIds();

        LocalDate currentDate = LocalDate.now();

        // 상품 조회
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductApplicationException(ProductErrorCode.NOT_EXIST_PRODUCT));

        List<Integer> couponIdsToList = Arrays.stream(couponIds)
                                    .boxed()
                                    .distinct()
                                    .collect(Collectors.toList());

        if (couponIds.length != couponIdsToList.size()) {
            throw new ProductApplicationException(PromotionErrorCode.DUPLICATED_PROMOTION);
        }

        List<PromotionProducts> promotionProducts = promotionProductsRepository.findWithPromotionByPromotionIdIn(couponIdsToList);

        // 프로모션 기한
        promotionProducts.stream()
            .filter(promotion -> !isPromotionPeriod(promotion.getPromotion().getUseStartedAt(), promotion.getPromotion().getUseEndedAt(), currentDate))
            .findFirst()
            .ifPresent(promotion -> {
                throw new ProductApplicationException(PromotionErrorCode.EXPIRED_PROMOTION_PERIOD);
            });

        List<PromotionProducts> promotion = promotionProducts.stream()
            .sorted(Comparator.comparingInt(a -> a.getPromotion().getDiscountType().getPriority()))
            .collect(Collectors.toList());

        String name = product.getName();
        int originPrice = product.getPrice();
        int discountSum = 0;

        // 상품 가격 계산
        for (PromotionProducts promotionProduct : promotion) {
            DiscountType discountType = promotionProduct.getPromotion().getDiscountType();

            int discountValue = promotionProduct.getPromotion().getDiscountValue();

            discountSum += discountType.applyDiscount(originPrice - discountSum, discountValue, discountType);
        }

        discountSum += getRemainingPrice(originPrice - discountSum);

        int totalPrice = originPrice - discountSum;
        validProductLimitPrice(totalPrice);

        return ProductAmountResponse.builder()
            .name(name)
            .originPrice(originPrice)
            .discountPrice(discountSum)
            .finalPrice(totalPrice)
            .build();
    }

    public boolean isPromotionPeriod(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
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
