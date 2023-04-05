package antigravity.domain.promotion;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
    name = "promotion_products",
    indexes = {
        @Index(name = "promotion_products_idx_01", columnList = "product_id"),
        @Index(name = "promotion_products_idx_02", columnList = "promotion_id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    PromotionProducts(long productId, Promotion promotion) {
        this.productId = productId;
        this.promotion = promotion;
    }
}
