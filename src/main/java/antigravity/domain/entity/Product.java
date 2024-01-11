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
    private int id;
    private String name;
    private int price;

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

    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("Product entity is empty or uninitialized.");
        }
    }
}
