package antigravity.domain.entity.promotion;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import antigravity.enums.promotion.DiscountType;
import antigravity.enums.promotion.PromotionType;
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
public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; //ID

	@Enumerated(EnumType.STRING)
	private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

	private String name; //프로모션 명

	@Enumerated(EnumType.STRING)
	private DiscountType discountType; // 할인 타입

	private Integer discountValue; // 할인 금액 or 할인 %

	private Date useStartedAt; // 쿠폰 사용가능 시작 기간

	private Date useEndedAt; // 쿠폰 사용가능 종료 기간
}
