package antigravity.domain.entity;

import antigravity.exception.EntityIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class Product {

    private static final Product EMPTY = new Product(0, "", BigDecimal.ZERO);

    @Id
    private int id;
    private String name;
    private BigDecimal price;

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public String getName() {
        checkIfEmpty();
        return name;
    }

    public BigDecimal getPrice() {
        checkIfEmpty();
        return price;
    }

    public String toString() {
        checkIfEmpty();
        return this.toString();
    }

    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("Product entity is empty or uninitialized.");
        }
    }
}
