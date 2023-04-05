package antigravity.controller.product.response;

import antigravity.service.product.resource.GetProductAmountResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GetProductAmountResponse {
    @JsonProperty("name")
    private final String name; //상품명
    @JsonProperty("originPrice")
    private final long originPrice; //상품 기존 가격
    @JsonProperty("discountPrice")
    private final long discountPrice; //총 할인 금액
    @JsonProperty("finalPrice")
    private final long finalPrice; //확정 상품 가격

    public GetProductAmountResponse(GetProductAmountResource resource) {
        this.name = resource.getName();
        this.originPrice = resource.getOriginPrice();
        this.discountPrice = resource.getDiscountPrice();
        this.finalPrice = resource.getFinalPrice();
    }
}
