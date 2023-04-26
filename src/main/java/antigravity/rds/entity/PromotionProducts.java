package antigravity.rds.entity;


import lombok.*;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
@Entity
@Table(name = "promotion_products")
@Alias("PromotionProducts")
public class PromotionProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "promotion_id")
    private Integer promotionId;

    @Column(name = "product_id")
    private Integer productId;
}
