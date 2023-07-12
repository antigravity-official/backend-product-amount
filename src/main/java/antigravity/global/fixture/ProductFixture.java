package antigravity.global.fixture;

import antigravity.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductFixture {
    VALID_PRODUCT1("남성 보정 속옷", 158500),
    VALID_PRODUCT2("여성 보정 속옷", 178300),
    VALID_PRODUCT3("값 비싼 속옷", 353990),
    VALID_PRODUCT4("더 비싼 속옷", 464960),
    VALID_PRODUCT5("조금 더 비싼 속옷", 720060),
    LOW_PRICE_PRODUCT("싸구려 속옷", 45000);

    private final String name;
    private final int price;

    public Product toEntity() {
        return Product.of(
                name,
                price
        );
    }
}
