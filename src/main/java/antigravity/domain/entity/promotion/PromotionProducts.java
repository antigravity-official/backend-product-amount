package antigravity.domain.entity.promotion;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "promotion_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    PromotionProducts(long productId, Promotion promotion) {
        this.productId = productId;
        this.promotion = promotion;
    }
}
