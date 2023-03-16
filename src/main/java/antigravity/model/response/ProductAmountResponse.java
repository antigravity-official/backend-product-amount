package antigravity.model.response;

import lombok.Builder;
import lombok.Data;

import java.text.DecimalFormat;

@Data
public class ProductAmountResponse {
    private String name; //상품명

    private int originPrice; //상품 기존 가격
    private int discountPrice; //총 할인 금액
    private String finalPrice; //확정 상품 가격

    @Builder
    public ProductAmountResponse(String name, int originPrice, int discountPrice, int finalPrice){
        DecimalFormat df = new DecimalFormat("###,###");

        this.name = name;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.finalPrice = df.format(finalPrice);
    }
}
