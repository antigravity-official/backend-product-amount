package antigravity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            final int discountAmount,
            final int finalPrice
    ) {
        return ProductAmountResponse.builder()
                .name(name)
                .originPrice(originPrice)
                .discountAmount(discountAmount)
                .finalPrice(finalPrice)
                .build();
    }
}
