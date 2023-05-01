package antigravity.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.enums.CutStandard;
import antigravity.enums.exception.ExceptionInfo;
import antigravity.enums.promotion.DiscountType;
import antigravity.enums.promotion.PromotionType;
import antigravity.exception.product.ProductNotFoundException;
import antigravity.exception.promotion.PromotionInvalidException;
import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.service.product.ProductServiceImpl;
import antigravity.util.CalculationUtil;
import antigravity.util.product.ProductPriceUtil;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest { //서비스 단위 테스트

	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepo;

	@Mock
	private PromotionRepository promotionRepo;

	@Mock
	private ProductPriceUtil productPriceUtil;

	// given
	private static Timestamp now;
	private static Timestamp yesterday;
	private static Timestamp tomorrow;
	private static Product product;
	private static Product product2;
	private static Promotion couponPromotion;
	private static Promotion codePromotion;
	private static Promotion expiredPromotion;

	@BeforeAll
	public static void getTimestamp() {
		now = Timestamp.valueOf(LocalDateTime.now());
		yesterday = Timestamp.valueOf(now.toLocalDateTime().minusDays(1));
		tomorrow = Timestamp.valueOf(now.toLocalDateTime().plusDays(1));
	}

	@BeforeAll
	public static void getProduct() {
		product = Product.builder()
			.id(1)
			.name("피팅노드상품")
			.price(215000)
			.build();
		product2 = Product.builder()
			.id(2)
			.name("피팅노드상품2")
			.price(43000)
			.build();
	}

	@BeforeAll
	public static void getCouponPromotion() {
		couponPromotion = Promotion.builder()
			.id(1)
			.promotionType(PromotionType.COUPON)
			.name("30000원 할인쿠폰")
			.discountType(DiscountType.WON)
			.discountValue(new BigDecimal(30000))
			.useStartedAt(yesterday)
			.useEndedAt(tomorrow)
			.build();
	}

	@BeforeAll
	public static void getCodePromotion() {
		codePromotion = Promotion.builder()
			.id(2)
			.promotionType(PromotionType.CODE)
			.name("15% 할인코드")
			.discountType(DiscountType.PERCENT)
			.discountValue(new BigDecimal(15))
			.useStartedAt(yesterday)
			.useEndedAt(tomorrow)
			.build();
	}

	@BeforeAll
	public static void getExpiredPromotion() {
		expiredPromotion = Promotion.builder()
			.id(1)
			.promotionType(PromotionType.COUPON)
			.name("1000원 할인쿠폰")
			.discountType(DiscountType.WON)
			.discountValue(new BigDecimal(1000))
			.useStartedAt(yesterday)
			.useEndedAt(yesterday)
			.build();
	}

	@Test
	@DisplayName("프로모션 제외 상품 가격 테스트")
	void 프로모션_제외_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder().productId(1).couponIds(null).build();
		final int productPrice = product.getPrice();
		//when
		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>());
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(0); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(productPrice); //최종 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			CalculationUtil.cutAmount(productPrice, CutStandard.THOUSANDS_CUT_STANDARD)); //최종 금액
	}

	@Test
	@DisplayName("금액 프로모션 적용 상품 가격 테스트")
	void 금액_프로모션_적용_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {1})
			.build();
		final int productPrice = product.getPrice();
		final int discountPrice = couponPromotion.getDiscountValue().intValue(); //금액 할인
		//when
		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(couponPromotion);
		}});
		when(productPriceUtil.adjustDiscountAmount(any(), anyInt())).thenReturn(
			discountPrice);
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(discountPrice); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			productPrice - discountPrice); //최종 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			CalculationUtil.cutAmount(productPrice - discountPrice, CutStandard.THOUSANDS_CUT_STANDARD)); //최종 금액
	}

	@Test
	@DisplayName("비율 프로모션 적용 상품 가격 테스트")
	void 비율_프로모션_적용_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {2})
			.build();
		//when
		final int productPrice = product.getPrice();
		final int discountPrice = (int)(productPrice * (codePromotion.getDiscountValue().intValue() / 100.0)); //비율 할인

		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(codePromotion);
		}});
		when(productPriceUtil.adjustDiscountAmount(any(), anyInt())).thenReturn(
			discountPrice);
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(discountPrice); //할인 금액
		assertThat(response.getFinalPrice()).isLessThan(
			productPrice - discountPrice); //절삭 전 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			CalculationUtil.cutAmount(productPrice - discountPrice, CutStandard.THOUSANDS_CUT_STANDARD)); //최종 금액
	}

	@Test
	@DisplayName("모든 프로모션 적용 상품 가격 테스트")
	void 모든_프로모션_적용_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {1, 2})
			.build();
		//when
		final int productPrice = product.getPrice();
		final int discountPrice = (int)(productPrice * (codePromotion.getDiscountValue().intValue() / 100.0))
			+ couponPromotion.getDiscountValue().intValue(); //비율 + 금액 할인
		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(codePromotion);
			add(couponPromotion);
		}});
		when(productPriceUtil.adjustDiscountAmount(any(), anyInt())).thenReturn(
			discountPrice);
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(discountPrice); //할인 금액
		assertThat(response.getFinalPrice()).isLessThan(
			productPrice - discountPrice); //절삭 전 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			CalculationUtil.cutAmount(productPrice - discountPrice, CutStandard.THOUSANDS_CUT_STANDARD)); //최종 금액
	}

	@Test
	@DisplayName("최소 상품 금액 테스트")
	void 최소_상품_금액_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(2)
			.couponIds(new Integer[] {1, 2})
			.build();
		//when
		final int productPrice = product2.getPrice();
		final int discountPrice = (int)(productPrice * (codePromotion.getDiscountValue().intValue() / 100.0))
			+ couponPromotion.getDiscountValue().intValue(); //비율 + 금액 할인
		final int minimumProductPrice = 10000; //최소 금액
		final int adjustedDiscountAmount = ((productPrice - discountPrice) < discountPrice) ?
			productPrice - minimumProductPrice : discountPrice; //조정 금액
		when(productRepo.findById(2)).thenReturn(Optional.of(product2));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(codePromotion);
			add(couponPromotion);
		}});
		when(productPriceUtil.adjustDiscountAmount(any(), anyInt())).thenReturn(
			adjustedDiscountAmount);
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product2.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(adjustedDiscountAmount); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			minimumProductPrice); //절삭 전 금액
		assertThat(response.getFinalPrice()).isEqualTo(
			CalculationUtil.cutAmount(minimumProductPrice, CutStandard.THOUSANDS_CUT_STANDARD)); //최종 금액
	}

	@Test
	@DisplayName("적용 대상 프로모션 아닌 경우 테스트")
	void 적용_대상_프로모션_아닌_경우_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {1, 3})
			.build();
		//when
		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(couponPromotion);
		}});
		//then
		final PromotionInvalidException exception = assertThrows(PromotionInvalidException.class,
			() -> productService.getProductAmount(request));
		final ExceptionInfo exceptionInfo = exception.getExceptionInfo();

		assertThat(exceptionInfo).isEqualTo(ExceptionInfo.PROMOTION_INVALID);
		assertThat(exceptionInfo.getCode()).isEqualTo(-200);
		assertThat(exceptionInfo.getMessage()).isEqualTo("유효하지 않은 프로모션입니다.");
	}

	@Test
	@DisplayName("존재하지 않는 상품 테스트")
	void 존재하지_않는_상품_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(3)
			.couponIds(null)
			.build();
		//when
		when(productRepo.findById(3)).thenReturn(Optional.empty());
		//then
		final ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
			() -> productService.getProductAmount(request));
		final ExceptionInfo exceptionInfo = exception.getExceptionInfo();

		assertThat(exceptionInfo).isEqualTo(ExceptionInfo.PRODUCT_NOT_FOUND);
		assertThat(exceptionInfo.getCode()).isEqualTo(-100);
		assertThat(exceptionInfo.getMessage()).isEqualTo("해당 상품 정보가 존재하지 않습니다.");
	}

	@Test
	@DisplayName("만료된 프로모션 적용 상품 가격 테스트")
	void 만료된_프로모션_적용_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {1, 3})
			.build();
		//when
		when(productRepo.findById(1)).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>() {{
			add(couponPromotion);
		}});
		//then
		final PromotionInvalidException exception = assertThrows(PromotionInvalidException.class,
			() -> productService.getProductAmount(request));
		final ExceptionInfo exceptionInfo = exception.getExceptionInfo();

		assertThat(exceptionInfo).isEqualTo(ExceptionInfo.PROMOTION_INVALID);
		assertThat(exceptionInfo.getCode()).isEqualTo(-200);
		assertThat(exceptionInfo.getMessage()).isEqualTo("유효하지 않은 프로모션입니다.");
	}

}