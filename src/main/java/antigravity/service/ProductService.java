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

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository repository;
    private final DiscountCalculationService discountCalculationService;
    private final PromotionValidationService promotionValidationService;

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

    private ProductAmountResponse buildProductAmountResponse(Product product, int discount) {
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discount)
                .finalPrice(roundFinalPrice(product.getPrice(), discount))
                .build();
    }

    private int roundFinalPrice(int finalPrice, int discount) {
        BigDecimal roundedFinalPrice = BigDecimal.valueOf(finalPrice)
                .subtract(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(1000), 0, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(1000));

        return roundedFinalPrice.intValue();
    }
}