package antigravity.domain.product;

import lombok.*;

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
        this.name = name;
        this.price = price;
    }
}
