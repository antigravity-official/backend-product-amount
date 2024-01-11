package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.service.DiscountCalculationService;
import antigravity.domain.service.PromotionValidationService;
import antigravity.exception.EntityNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.request.ProductInfoWrapperRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.utils.ProductInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Application service for orchestrating high-level operations.
 */
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final DiscountCalculationService discountCalculationService;
    private final PromotionValidationService promotionValidationService;

    /**
     * Retrieves and calculates the total amount for a product based on promotions.
     *
     * @param request The ProductInfoRequest containing product and promotion details.
     * @return A ProductAmountResponse with detailed pricing information.
     * @throws EntityNotFoundException If the product or promotions are not found.
     */
    @Transactional
    public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
        final ProductInfoWrapperRequest wrapperRequest = ProductInfoMapper.mapToWrapperRequest(request);

        final int productId = wrapperRequest.getProductId();
        final Product product = repository
                .getProduct(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found."));

        final List<Integer> couponIds = wrapperRequest.getCouponIds();
        final boolean promosAreValid = promotionValidationService.validatePromotion(product.getId(), couponIds);

        if (!promosAreValid) {
            throw new EntityNotFoundException("Promotions are invalid or non-existent");
        }

        final int discountAmount = discountCalculationService
                .calculateDiscountAmount(product.getPrice(), couponIds);

        return buildProductAmountResponse(product, discountAmount);
    }

    /**
     * Builds a response containing product pricing information after applying discounts.
     *
     * @param product The product for which the response is being built.
     * @param discount The discount amount to be applied.
     * @return ProductAmountResponse with the calculated pricing details.
     */
    private ProductAmountResponse buildProductAmountResponse(Product product, int discount) {
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discount)
                .finalPrice(roundFinalPrice(product.getPrice(), discount))
                .build();
    }

    /**
     * Rounds the final price to the nearest thousand after applying the discount.
     *
     * @param finalPrice The price before rounding.
     * @param discount The discount amount to be subtracted from the final price.
     * @return The rounded final price.
     */
    private int roundFinalPrice(int finalPrice, int discount) {
        BigDecimal roundedFinalPrice = BigDecimal.valueOf(finalPrice)
                .subtract(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(1000), 0, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(1000));

        return roundedFinalPrice.intValue();
    }
}