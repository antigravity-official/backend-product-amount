package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.model.dto.ProductDto;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDto getProduct(int productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductApplicationException(ProductErrorCode.NOT_EXIST_PRODUCT));

        return ProductDto.from(product);
    }
}
