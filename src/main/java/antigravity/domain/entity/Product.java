package antigravity.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private int price;

    /* static factory method */
    public static Product of(
            final String name,
            final int price
    ) {
        return Product.builder()
                .name(name)
                .price(price)
                .build();
    }
}
