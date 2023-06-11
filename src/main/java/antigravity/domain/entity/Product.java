package antigravity.domain.entity;

import antigravity.enums.ErrorResponse;
import antigravity.exception.CommonException;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Product {
    private int id;
    private String name;
    private int price;

    public int getPrice() {
        int price = this.price;

        if(price < 10000) {
            throw new CommonException(ErrorResponse.MIN_PRODUCT_PRICE);
        }else if(price > 10000000) {
            throw new CommonException(ErrorResponse.MAX_PRODUCT_PRICE);
        }

        price = price - (price % 1000);

        return price;
    }
}
