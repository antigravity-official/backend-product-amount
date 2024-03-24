package antigravity.model.response;

import antigravity.domain.entity.product.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties({"MIN_PURCHASABLE", "MAX_PURCHASABLE"})
public class ProductAmountResponse {
    private String name; //상품명

    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private int finalPrice; //확정 상품 가격
    private boolean isPurchasableRightAway; //바로 구매 가능 여부

    private static final int MIN_PURCHASABLE = 10_000;
    private static final int MAX_PURCHASABLE = 10_000_000;


    @Builder
    private ProductAmountResponse(String name, int originPrice, int discountPrice, int finalPrice, boolean isPurchasableRightAway) {
        this.name = name;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.isPurchasableRightAway = isPurchasableRightAway;
    }

    public static ProductAmountResponse of(Product product, int discountPrice) {
        int finalPrice = product.getFinalPrice();
        int originPrice = product.getPrice();

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountPrice(discountPrice)
                .finalPrice(discountPrice == 0 ? originPrice : finalPrice)
                .isPurchasableRightAway(finalPrice >= MIN_PURCHASABLE && finalPrice <= MAX_PURCHASABLE)
                .build();
    }
}
