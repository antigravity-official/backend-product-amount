package antigravity.domain.product;

public class ProductFixture {

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
                .name("name")
                .price(100_000L);
    }
}
