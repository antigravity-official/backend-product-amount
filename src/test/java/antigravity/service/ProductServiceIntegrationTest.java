package antigravity.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.service.product.ProductServiceImpl;
import antigravity.util.product.ProductPriceUtil;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTest { //서비스 통합 테스트

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private PromotionRepository promotionRepo;

	@Autowired
	private ProductPriceUtil productPriceUtil;

	private ProductServiceImpl productService;

	@BeforeEach
	public void setUp() {
		productService = new ProductServiceImpl(productRepo, promotionRepo, productPriceUtil);
	}

	@Test
	@DisplayName("프로모션 제외 상품 가격 테스트")
	void 프로모션_제외_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder().productId(1).couponIds(null).build();
		//when
		final ProductAmountResponse response = productService.getProductAmount(request);
		//then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("피팅노드상품"); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(215000); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(0); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(215000); //최종 금액
	}

	@Test
	@DisplayName("금액 프로모션 적용 상품 가격 테스트")
	void 금액_프로모션_적용_상품_가격_테스트() {
		//given
		final ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new Integer[] {1})
			.build();
		//when
		final ProductAmountResponse response = productService.getProductAmount(request);
		//then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("피팅노드상품"); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(215000); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(30000); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(185000); //최종 금액
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
		final ProductAmountResponse response = productService.getProductAmount(request);
		//then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("피팅노드상품"); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(215000); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(32250); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(182000); //최종 금액
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
		final ProductAmountResponse response = productService.getProductAmount(request);
		//then
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("피팅노드상품"); //상품명
		assertThat(response.getOriginPrice()).isEqualTo(215000); //상품 기존 금액
		assertThat(response.getDiscountPrice()).isEqualTo(62250); //할인 금액
		assertThat(response.getFinalPrice()).isEqualTo(152000); //최종 금액
	}

}
