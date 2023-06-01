package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.error.BusinessException;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final PromotionService promotionService;
    private final DiscountService discountService;
    private final ProductRepository productRepository;

    public ProductAmountResponse getProductAmount(int productId) {
        Product product = findProductByProductId(productId);
        List<Promotion> promotions = promotionService.findPromotionsByProductId(productId);
        return discountService.calculateProductAmount(product, promotions);
    }

    public Product findProductByProductId(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_EXIST_PRODUCT));
    }

    public int createProduct(Product product) {
        return productRepository.save(product).getId();
    }
}
