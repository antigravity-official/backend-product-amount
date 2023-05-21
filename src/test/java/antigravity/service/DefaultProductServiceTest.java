package antigravity.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

@SpringBootTest
@DisplayName("service 통합 테스트")
class DefaultProductServiceTest {

	@Autowired
	private DefaultProductService productService;

	@Test
	void getProductAmount() {
		// Given
		long[] couponIds = {1, 2};
		ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(couponIds)
			.build();

		// When
		ProductAmountResponse response = productService.getProductAmount(request);

		// Then
		Assertions.assertEquals("피팅노드상품", response.name());
		Assertions.assertEquals(215000, response.originPrice());
		Assertions.assertEquals(63000, response.discountPrice());
		Assertions.assertEquals(152000, response.finalPrice());
	}

}