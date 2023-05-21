package antigravity.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PromotionProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Promotion promotion;

	@ManyToOne
	private Product product;

	@Builder
	public PromotionProducts(Long id, Promotion promotion, Product product) {
		Assert.notNull(product, "product가 입력되지않았습니다.");
		Assert.notNull(promotion, "promotion이 입력되지않았습니다.");
		this.id = id;
		this.promotion = promotion;
		this.product = product;
	}
}
