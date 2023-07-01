package antigravity.fixture;

import antigravity.domain.entity.Product;
import java.util.Optional;

public class ProductFixture {
    public static Product getProduct() {
        return Product.builder()
            .name("product name")
            .price(100000)
            .build();
    }
}
