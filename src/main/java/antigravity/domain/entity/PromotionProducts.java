package antigravity.domain.entity;

import antigravity.exception.EntityIsEmptyException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class PromotionProducts {

    private static final PromotionProducts EMPTY = new PromotionProducts(0, 0, 0);

    @Id
    private int id;
    private int promotionId;
    private int productId;

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public int getPromotionId() {
        checkIfEmpty();
        return promotionId;
    }

    public int getProductId() {
        checkIfEmpty();
        return productId;
    }

    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("PromotionProduct entity is empty or uninitialized.");
        }
    }
}
