package antigravity.service;

import static org.assertj.core.api.Assertions.*;
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
import antigravity.enums.promotion.DiscountType;
import antigravity.enums.promotion.PromotionType;
import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.service.product.ProductServiceImpl;
import antigravity.util.product.ProductPriceUtil;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

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
	private static Promotion couponPromotion;
	private static Promotion codePromotion;

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

	@Test
	@DisplayName("적용 프로모션 제외 상품 가격 조회")
	void 적용_프로모션_제외_상품_가격_조회() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder().productId(1).couponIds(null).build();
		final int productPrice = product.getPrice();
		//when
		when(productRepo.findById(any())).thenReturn(Optional.of(product));
		when(promotionRepo.findAllByProductAndCouponIds(
			any(), any(), any())).thenReturn(new ArrayList<>());
		//then
		final ProductAmountResponse response = productService.getProductAmount(request);

		assertThat(response.getName()).isEqualTo(product.getName()); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(productPrice); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(0); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(productPrice); //최종 금액
	}

	@Test
	@DisplayName("금액 프로모션 적용 상품 가격 조회")
	void 금액_프로모션_적용_상품_가격_조회() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder().productId(1).couponIds(null).build();
		final int productPrice = product.getPrice();
		final int discountPrice = couponPromotion.getDiscountValue().intValue();
		//when
		when(productRepo.findById(any())).thenReturn(Optional.of(product));
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
	}

	@Test
	@DisplayName("비율 프로모션 적용 상품 가격 조회")
	void 비율_프로모션_적용_상품_가격_조회() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder().productId(1).couponIds(null).build();
		//when
		final int productPrice = product.getPrice();
		final int discountPrice = productPrice * (codePromotion.getDiscountValue().intValue() / 100);
		when(productRepo.findById(any())).thenReturn(Optional.of(product));
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
		assertThat(response.getFinalPrice()).isEqualTo(
			productPrice - discountPrice); //최종 금액
	}

}