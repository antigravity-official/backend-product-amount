package antigravity.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductAmountResponse {
    private String name;        // 상품명
    private int originPrice;    // 상품 기존 가격
    private int discountAmount;  // 총 할인 금액
    private int finalPrice;      // 확정 상품 가격

    /* static factory method */
    public static ProductAmountResponse of(
            final String name,
            final int originPrice,
            final int discountedAmount,
            final int finalPrice
    ) {
        return ProductAmountResponse.builder()
                .name(name)
                .originPrice(originPrice)
                .discountAmount(discountedAmount)
                .finalPrice(finalPrice)
                .build();
    }
}
