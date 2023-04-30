package antigravity.domain.entity.promotion;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import antigravity.domain.entity.common.BaseEntity;
import antigravity.enums.promotion.DiscountType;
import antigravity.enums.promotion.PromotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion extends BaseEntity {

	@Enumerated(EnumType.STRING)
	private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

	private String name; //프로모션 명

	@Enumerated(EnumType.STRING)
	private DiscountType discountType; // 할인 타입

	private BigDecimal discountValue; // 할인 금액 or 할인 %

	private Timestamp useStartedAt; // 쿠폰 사용가능 시작 기간

	private Timestamp useEndedAt; // 쿠폰 사용가능 종료 기간
}
