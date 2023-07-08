package antigravity.model.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductAmountResponse {
    private String name; //상품명

    private BigDecimal originPrice; //상품 기존 가격
    private BigDecimal discountPrice; //총 할인 금액
    private BigDecimal finalPrice; //확정 상품 가격
}
