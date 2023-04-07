package antigravity.domain.product;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private long price;

    @Builder
    public Product(String name, long price) {
        setName(name);
        setPrice(price);
    }

    private void setName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("name must not be empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("name size must be 255 or less");
        }
        this.name = name;
    }

    private void setPrice(long price) {
        if (price < 0) {
            throw new IllegalArgumentException("price must be 1 won or more");
        }
        this.price = price;
    }
}
