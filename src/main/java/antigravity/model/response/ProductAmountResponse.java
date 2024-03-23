package antigravity.model.response;

import antigravity.domain.entity.product.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductAmountResponse {
    private String name; //상품명

    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격

    @Builder
    private ProductAmountResponse(String name, int originPrice, int discountPrice, int finalPrice) {
        this.name = name;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
    }

    public static ProductAmountResponse of(Product product, int discountPrice, int finalPrice) {
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }
}
