package antigravity.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
@Table(name = "promotion_products")
@AllArgsConstructor
public class PromotionProducts {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column
    private int promotionId;

    @Column
    private int productId;

    /* static factory method */
    public static PromotionProducts of(
            final int promotionId,
            final int productId
    ) {
        return PromotionProducts.builder()
                .promotionId(promotionId)
                .productId(productId)
                .build();
    }
}
