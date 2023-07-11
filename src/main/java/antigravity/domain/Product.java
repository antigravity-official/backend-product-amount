package antigravity.domain;

import lombok.*;

import javax.persistence.*;

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
    @Column
    private int id;

    @Column
    private String name;

    @Column
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
