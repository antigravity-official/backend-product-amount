package antigravity.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import antigravity.controller.product.ProductController;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.service.product.ProductServiceImpl;

@WebMvcTest(ProductController.class)
public class ProductControllerTest { //컨트롤러 단위 테스트

	@Autowired
	MockMvc mvc;

	@MockBean
	ProductServiceImpl productService;

	@Test
	@DisplayName("프로모션 제외 상품 가격 테스트")
	void 프로모션_제외_상품_가격_테스트() throws Exception {
		when(productService.getProductAmount(any()))
			.thenReturn(ProductAmountResponse.builder()
				.name("피팅노드상품")
				.originPrice(215000)
				.discountPrice(0)
				.finalPrice(215000)
				.build());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1/amount"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("피팅노드상품"))
			.andExpect(jsonPath("originPrice").value(215000))
			.andExpect(jsonPath("discountPrice").value(0))
			.andExpect(jsonPath("finalPrice").value(215000));
	}

	@Test
	@DisplayName("금액 프로모션 적용 상품 가격 테스트")
	void 금액_프로모션_적용_상품_가격_테스트() throws Exception {
		when(productService.getProductAmount(any()))
			.thenReturn(ProductAmountResponse.builder()
				.name("피팅노드상품")
				.originPrice(215000)
				.discountPrice(30000)
				.finalPrice(185000)
				.build());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1/amount").param("couponIds", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("피팅노드상품"))
			.andExpect(jsonPath("originPrice").value(215000))
			.andExpect(jsonPath("discountPrice").value(30000))
			.andExpect(jsonPath("finalPrice").value(185000));
	}

	@Test
	@DisplayName("비율 프로모션 적용 상품 가격 테스트")
	void 비율_프로모션_적용_상품_가격_테스트() throws Exception {
		when(productService.getProductAmount(any()))
			.thenReturn(ProductAmountResponse.builder()
				.name("피팅노드상품")
				.originPrice(215000)
				.discountPrice(32250)
				.finalPrice(182000)
				.build());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1/amount").param("couponIds", "2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("피팅노드상품"))
			.andExpect(jsonPath("originPrice").value(215000))
			.andExpect(jsonPath("discountPrice").value(32250))
			.andExpect(jsonPath("finalPrice").value(182000));
	}

	@Test
	@DisplayName("모든 프로모션 적용 상품 가격 테스트")
	void 모든_프로모션_적용_상품_가격_테스트() throws Exception {
		when(productService.getProductAmount(any()))
			.thenReturn(ProductAmountResponse.builder()
				.name("피팅노드상품")
				.originPrice(215000)
				.discountPrice(62250)
				.finalPrice(152000)
				.build());
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/products/1/amount").param("couponIds", "1,2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("name").value("피팅노드상품"))
			.andExpect(jsonPath("originPrice").value(215000))
			.andExpect(jsonPath("discountPrice").value(62250))
			.andExpect(jsonPath("finalPrice").value(152000));
	}

}
