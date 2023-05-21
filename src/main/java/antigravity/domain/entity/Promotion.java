package antigravity.domain.entity;

import antigravity.domain.DiscountType;
import antigravity.domain.PromotionType;
import antigravity.exception.NotAvailableDatePromotionException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Promotion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인

	@Column(nullable = false)
	private int discountValue; // 할인 금액 or 할인 %

	@Column(nullable = false)
	private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간

	@Column(nullable = false)
	private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

	@Builder
	public Promotion(Long id, PromotionType promotionType, String name, DiscountType discountType,
		Integer discountValue,
		LocalDate useStartedAt, LocalDate useEndedAt) {
		validatePromotionType(promotionType);
		validateName(name);
		validateDiscountValue(discountValue);
		validatePeriod(useStartedAt, useEndedAt);
		this.id = id;
		this.promotionType = promotionType;
		this.name = name;
		this.discountType = discountType == null ? promotionType.getDiscountType() : discountType;
		this.discountValue = discountValue;
		this.useStartedAt = useStartedAt;
		this.useEndedAt = useEndedAt;
	}

	private void validatePeriod(LocalDate useStartedAt, LocalDate useEndedAt) {
		Assert.notNull(useStartedAt, "프로모션 시작 기간이 입력되지 않았습니다.");
		Assert.notNull(useEndedAt, "프로모션 종료 기간이 입력되지 않았습니다.");
		Assert.isTrue(useEndedAt.compareTo(useStartedAt) >= 0,
			"프로모션 시작 기간은 종료 기간보다 이전일 수 없습니다.");
	}

	private void validateDiscountValue(Integer discountValue) {
		Assert.notNull(discountValue, "프로모션 할인 값이 입력되지 않았습니다.");
	}

	private void validateName(String name) {
		Assert.hasText(name, "프로모션 이름이 입력되지 않았습니다.");
		Assert.isTrue(name.length() <= 255, "프로모션 이름은 최대 255자입니다.");
	}

	private void validatePromotionType(PromotionType promotionType) {
		Assert.notNull(promotionType, "프로모션 타입이 입력되지 않았습니다.");
	}

	public int calculateDiscountPrice(int productPrice) {
		validateExpirationPeriod();
		if (Objects.equals(promotionType, PromotionType.COUPON)) {
			return discountValue;
		} else {
			return (int)(productPrice * ((double)discountValue / 100));
		}
	}

	public void validateExpirationPeriod() {
		LocalDate now = LocalDate.now();
		if (now.compareTo(useStartedAt) < 0 || now.compareTo(useEndedAt) > 0){
			throw new NotAvailableDatePromotionException();
		}
	}
}
