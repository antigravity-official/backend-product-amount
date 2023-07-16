package antigravity.domain.reader;

import antigravity.domain.entity.Product;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductReaderImpl implements ProductReader{
    private final ProductRepository productRepository;

    @Override
    public Product getProduct(Integer productId) {

        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
        );
    }
}
