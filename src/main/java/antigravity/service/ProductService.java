package antigravity.service;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;


    @Cacheable(value = "getProductAmountCache", key = "#request.toString() + #now.toString()")
    public ProductAmountResponse getProductAmount(ProductInfoRequest request, LocalDate now) {
        Product product = findProductById(request.getProductId());
        List<Promotion> promotions = findPromotionsByIds(request.getCouponIds());

        int discountPrice = product.getDiscountsByPromotions(promotions, now);
        product.finalizePrice(discountPrice);

        return ProductAmountResponse.of(product, discountPrice);
    }

    private List<Promotion> findPromotionsByIds(int[] promotionIds) {
        List<Promotion> promotions = promotionRepository.findAllByIdIn(promotionIds);
        if(promotions.size() != promotionIds.length) {
            throw new RuntimeException("some promotions not found");
        }
        return promotions;
    }

    private Product findProductById(int productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));
    }
}
