package antigravity.controller.product.response;

import antigravity.service.product.resource.GetProductAmountResource;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class GetProductAmountResponse {
    @JsonProperty("name")
    private String name; //상품명
    @JsonProperty("originPrice")
    private long originPrice; //상품 기존 가격
    @JsonProperty("discountPrice")
    private long discountPrice; //총 할인 금액
    @JsonProperty("finalPrice")
    private long finalPrice; //확정 상품 가격

    public GetProductAmountResponse(GetProductAmountResource resource) {
        setName(resource.getName());
        setOriginPrice(resource.getOriginPrice());
        setDiscountPrice(resource.getDiscountPrice());
        setFinalPrice(resource.getFinalPrice());
    }

    private void setName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name must not be empty");
        }
        this.name = name;
    }

    private void setOriginPrice(long originPrice) {
        if (originPrice < 1) {
            throw new IllegalArgumentException("originPrice must be 1 or more");
        }
        this.originPrice = originPrice;
    }

    private void setDiscountPrice(long discountPrice) {
        if (discountPrice < 0) {
            throw new IllegalArgumentException("discountPrice must be 0 or more");
        }
        this.discountPrice = discountPrice;
    }

    private void setFinalPrice(long finalPrice) {
        if (finalPrice < 1) {
            throw new IllegalArgumentException("finalPrice must be 1 or more");
        }
        this.finalPrice = finalPrice;
    }
}
