package antigravity.domain.entity;

import antigravity.exception.EntityIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
public class Product {

    private static final Product EMPTY = new Product(0, "", 0);

    @Id
    private int id;         // 상품 ID
    private String name;    // 상품명
    private int price;      // 상품 가격

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public String getName() {
        checkIfEmpty();
        return name;
    }

    public int getPrice() {
        checkIfEmpty();
        return price;
    }

    /**
     * Checks if this instance is the same as EMPTY.
     *
     * @throws EntityIsEmptyException if this instance is the EMPTY
     */
    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("Product entity is empty or uninitialized.");
        }
    }
}