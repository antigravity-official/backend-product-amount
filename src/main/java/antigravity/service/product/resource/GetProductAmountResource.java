package antigravity.service.product.resource;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
public class GetProductAmountResource {
    private String name;
    private long originPrice;
    private long discountPrice;
    private long finalPrice;

    public GetProductAmountResource(String name, long originPrice, long discountPrice, long finalPrice) {
        setName(name);
        setOriginPrice(originPrice);
        setDiscountPrice(discountPrice);
        setFinalPrice(finalPrice);
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
