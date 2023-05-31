package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
@Table(name = "promotion_products")
public class PromotionProducts {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private int promotionId;
    private int productId;
}
