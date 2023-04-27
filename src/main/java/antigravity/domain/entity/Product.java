package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private int id;
    private String name;
    private int price;

    public void validateProductPrice() {
        if (price < 1000) {
            throw new RuntimeException("최소 금액보다 작습니다.");
        } else if (price > 10000000) {
            throw new RuntimeException("최대 금액보다 큽니다.");
        }
    }
}
