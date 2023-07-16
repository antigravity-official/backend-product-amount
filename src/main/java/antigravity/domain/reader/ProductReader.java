package antigravity.domain.reader;

import antigravity.domain.entity.Product;

public interface ProductReader {
    Product getProduct(Integer productId);
}
