package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.service.DiscountCalculationService;
import antigravity.domain.service.PromotionValidationService;
import antigravity.exception.EntityNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.request.ProductInfoWrapperRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.utils.RequestResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        final ProductInfoWrapperRequest wrapperRequest = RequestResponseMapper.mapToWrapperRequest(request);

        // 1. Get product
        final int productId = wrapperRequest.getProductId();
        final Product product = repository
                .getProduct(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with ID " + productId + " not found."));

        // 2. Validate promotions -> product
        final List<Integer> couponIds = wrapperRequest.getCouponIds();
        promotionValidationService.validatePromotion(product.getId(), couponIds);

        // 3. Apply promotions for discount price
        final int discountPrice = discountCalculationService
                .calculateDiscountAmount(product.getPrice(), couponIds);

        return RequestResponseMapper.mapToAmountResponse(product, discountPrice);
    }
}