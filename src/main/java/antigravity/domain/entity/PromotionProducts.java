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
    private int id;             // 쿠폰-상품 ID
    private int promotionId;    // 쿠폰 ID
    private int productId;      // 상품 ID

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public int getPromotionId() {
        checkIfEmpty();
        return promotionId;
    }

    /**
     * Checks if this instance is the same as EMPTY.
     *
     * @throws EntityIsEmptyException if this instance is the EMPTY
     */
    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("PromotionProduct entity is empty or uninitialized.");
        }
    }
}