package antigravity.fixture;

import antigravity.domain.entity.Product;

public class ProductFixture {
    public static Product getProduct() {
        return Product.builder()
            .id(1)
            .name("피팅노드 상품 [1]")
            .price(215000)
            .build();
    }

    public static Product getProduct2() {
        return Product.builder()
            .id(2)
            .name("피팅노드 상품 [2]")
            .price(100000)
            .build();
    }


    public static Product getMaxProduct() {
        return Product.builder()
            .id(3)
            .name("피팅노드 상품 [3]")
            .price(19920000)
            .build();
    }
}
