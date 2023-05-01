package antigravity.domain.entity.promotion_products;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionProducts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //상품 프로모션 ID

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "promotion_id")
	private Promotion promotion; //프로모션

	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product; //상품

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Timestamp createdAt; //생성일시

	@Override
	public String toString() {
		return "PromotionProducts{" +
			"id=" + id +
			", promotion=" + promotion.getId() +
			", product=" + product.getId() +
			", createdAt=" + createdAt +
			'}';
	}
}
