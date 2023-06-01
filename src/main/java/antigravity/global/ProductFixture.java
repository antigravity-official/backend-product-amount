package antigravity.global;

import antigravity.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductFixture {
    VALID_PRODUCT1("남성보정속옷", 158500),
    VALID_PRODUCT2("여성보정속옷", 178500);

    private final String name;
    private final int price;

    public Product toEntity() {
        return Product.of(
                name,
                price
        );
    }
}
