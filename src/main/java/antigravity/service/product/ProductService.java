package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.PromotionProducts;
import antigravity.error.BusinessException;
import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotionproducts.PromotionProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductQueryRepository productQueryRepository;
    private final PromotionProductsRepository promotionProductsRepository;
    private final ProductRepository productRepository;

    public Product findProductByProductId(Integer productId) {
        return productQueryRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_EXIST_PRODUCT));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void createProductPromotionMapping(PromotionProducts promotionProducts){
        promotionProductsRepository.save(promotionProducts);
    }
}
