package antigravity.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductAmountResponse {

    private String name; //상품명
    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    private ProductAmountResponse() {
    }

    public static ProductAmountResponse of(String name, int price) {
        ProductAmountResponse productAmountResponse = new ProductAmountResponse();
        productAmountResponse.name = name;
        productAmountResponse.originPrice = price;
        return productAmountResponse;
    }

    public void applyDiscountPolicy(int finalPrice, int discountPrice) {
        this.finalPrice = finalPrice;
        this.discountPrice = discountPrice;
    }
}
