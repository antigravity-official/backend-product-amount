package antigravity.domain.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import antigravity.domain.DiscountType;
import antigravity.domain.PromotionType;
import antigravity.exception.NotAvailableDatePromotionException;

public class PromotionTest {

	@Test
	@DisplayName("promotion 생성 성공")
	public void testPromotionConstructor() {
		Long id = 1L;
		PromotionType promotionType = PromotionType.COUPON;
		String name = "Test Promotion";
		Integer discountValue = 5000;
		LocalDate useStartedAt = LocalDate.of(2023, 1, 1);
		LocalDate useEndedAt = LocalDate.of(2023, 12, 31);

		Promotion promotion = Promotion.builder()
			.id(id)
			.promotionType(promotionType)
			.name(name)
			.discountValue(discountValue)
			.useStartedAt(useStartedAt)
			.useEndedAt(useEndedAt)
			.build();

		Assertions.assertEquals(id, promotion.getId());
		Assertions.assertEquals(promotionType, promotion.getPromotionType());
		Assertions.assertEquals(name, promotion.getName());
		Assertions.assertEquals(promotionType.getDiscountType(), promotion.getDiscountType());
		Assertions.assertEquals(discountValue, promotion.getDiscountValue());
		Assertions.assertEquals(useStartedAt, promotion.getUseStartedAt());
		Assertions.assertEquals(useEndedAt, promotion.getUseEndedAt());
	}

	@ParameterizedTest
	@DisplayName("promotion 생성 실패 - name이 유효하지 않을 경우")
	@ValueSource(strings = {" ", ""})
	@NullSource
	public void testPromotionValidation_name(String name) {

		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Promotion.builder()
			.promotionType(PromotionType.COUPON)
			.name(name)
			.discountValue(5000)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now())
			.build()
		);
	}

	@ParameterizedTest
	@DisplayName("promotion 생성 실패 - type이 유효하지 않을 경우")
	@NullSource
	public void testPromotionValidation_promotionType(PromotionType promotionType) {

		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Promotion.builder()
				.promotionType(promotionType)
				.name("Test Promotion")
				.discountValue(5000)
				.useStartedAt(LocalDate.now())
				.useEndedAt(LocalDate.now())
				.build()
		);
	}

	@ParameterizedTest
	@DisplayName("promotion 생성 실패 - value가 유효하지 않을 경우")
	@NullSource
	public void testPromotionValidation_discountValue(Integer discountValue) {

		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Promotion.builder()
				.promotionType(PromotionType.CODE)
				.name("Test Promotion")
				.discountValue(discountValue)
				.useStartedAt(LocalDate.now())
				.useEndedAt(LocalDate.now())
				.build()
		);
	}

	@Test
	@DisplayName("promotion 생성 실패 - 종료 기간이 시작 기간보다 빠를 경우")
	public void testPromotionValidation_userPeriod() {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Promotion.builder()
				.promotionType(PromotionType.CODE)
				.name("Test Promotion")
				.discountValue(30)
				.useStartedAt(LocalDate.now())
				.useEndedAt(LocalDate.now().minusDays(1))
				.build()
		);
	}

	@Test
	@DisplayName("testCalculateDiscountAmount 성공")
	public void testCalculateDiscountAmount() {

		int productPrice = 10000;

		// Coupon promotion with won discount
		Promotion couponPromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.COUPON)
			.discountValue(500)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now().plusDays(1))
			.build();

		Assertions.assertEquals(500, couponPromotion.calculateDiscountPrice(productPrice));

		// Coupon promotion with won discount
		Promotion wonPromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.COUPON)
			.discountType(DiscountType.WON)
			.discountValue(3000)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now().plusDays(1))
			.build();

		Assertions.assertEquals(3000, wonPromotion.calculateDiscountPrice(productPrice));

		// Code promotion with percent discount
		Promotion codePromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.CODE)
			.discountValue(20)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now().plusDays(1))
			.build();

		Assertions.assertEquals(2000, codePromotion.calculateDiscountPrice(productPrice));

		// Code promotion with percent discount
		Promotion percentPromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.CODE)
			.discountValue(38)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now().plusDays(1))
			.build();

		Assertions.assertEquals(3800, percentPromotion.calculateDiscountPrice(productPrice));
	}

	@Test
	@DisplayName("testValidateExpirationPeriod 성공")
	public void testValidateExpirationPeriod() {
		Promotion validPromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.COUPON)
			.discountType(DiscountType.WON)
			.discountValue(5000)
			.useStartedAt(LocalDate.now())
			.useEndedAt(LocalDate.now().plusDays(3))
			.build();

		// Valid period
		Assertions.assertDoesNotThrow(validPromotion::validateExpirationPeriod);
	}

	@Test
	@DisplayName("testValidateExpirationPeriod 실패")
	public void testValidateExpirationPeriod_fail() {

		// Expired promotion
		Promotion expiredPromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.COUPON)
			.discountType(DiscountType.WON)
			.discountValue(5000)
			.useStartedAt(LocalDate.of(2022, 1, 1))
			.useEndedAt(LocalDate.of(2022, 12, 31))
			.build();

		Assertions.assertThrows(NotAvailableDatePromotionException.class, expiredPromotion::validateExpirationPeriod);

		// Future promotion
		Promotion futurePromotion = Promotion.builder()
			.name("Test Promotion")
			.promotionType(PromotionType.COUPON)
			.discountType(DiscountType.WON)
			.discountValue(5000)
			.useStartedAt(LocalDate.now().plusMonths(1))
			.useEndedAt(LocalDate.now().plusMonths(1))
			.build();

		Assertions.assertThrows(NotAvailableDatePromotionException.class, futurePromotion::validateExpirationPeriod);
	}
}
