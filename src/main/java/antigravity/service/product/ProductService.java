package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.error.BusinessException;
import antigravity.repository.product.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductQueryRepository productQueryRepository;

    public Product findProductById(Integer productId) {
        return productQueryRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(NOT_EXIST_PRODUCT));
    }
}
