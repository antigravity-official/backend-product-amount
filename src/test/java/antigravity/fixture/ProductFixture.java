package antigravity.fixture;

import antigravity.domain.entity.Product;

public class ProductFixture {
    public static Product getProduct() {
        return Product.builder()
            .id(1)
            .name("product name")
            .price(100000)
            .build();
    }

    public static Product getProduct2() {
        return Product.builder()
            .id(2)
            .name("product name")
            .price(100000)
            .build();
    }

    public static Product getMaxProduct() {
        return Product.builder()
            .id(3)
            .name("product name")
            .price(10092000)
            .build();
    }
}
