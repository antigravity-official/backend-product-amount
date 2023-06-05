package antigravity.model.response;

import antigravity.domain.entity.Product;
import lombok.Data;

@Data
public class ProductAmountResponse {
    private String name; //상품명

    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    public ProductAmountResponse() {
    }

    public static ProductAmountResponse from(
            Product product,
            Integer discountSum
    ) {
        ProductAmountResponse response = new ProductAmountResponse();
        response.setName(product.getName());
        response.setOriginPrice(product.getPrice());
        response.setDiscountPrice(discountSum);
        response.setFinalPrice(
                ((product.getPrice() - discountSum) / 1000) * 1000);
        return response;
    }
}
