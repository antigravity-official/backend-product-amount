package antigravity.fixture;

import antigravity.domain.entity.Product;

public class ProductFixture {
    public static Product getProduct() {
        return Product.builder()
            .name("product name")
            .price(100000)
            .build();
    }

    public static Product getMaxProduct() {
        return Product.builder()
            .name("product name")
            .price(10092000)
            .build();
    }
}
