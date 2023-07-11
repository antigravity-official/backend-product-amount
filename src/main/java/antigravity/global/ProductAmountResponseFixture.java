package antigravity.global;

import antigravity.domain.Product;
import antigravity.dto.response.ProductAmountResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductAmountResponseFixture {
    VALID("피팅노드상품", 158500, 54500, 104000),
    NO_DISCOUNT("할인 없음", 10000, 0, 10000);

    private final String name;
    private final int originPrice;
    private final int discountAmount;
    private final int finalPrice;

    public ProductAmountResponse toEntity() {
        return ProductAmountResponse.of(
                name,
                originPrice,
                discountAmount,
                finalPrice
        );
    }
}
