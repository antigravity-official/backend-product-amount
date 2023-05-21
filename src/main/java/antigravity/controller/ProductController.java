package antigravity.controller;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.DefaultProductService;
import antigravity.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	/**
	 * <Pre>
	 * 상품 가격 추출 api
	 * </Pre>
	 *
	 * @return ProductAmountResponse
	 */
	@GetMapping("/amount")
	public ResponseEntity<ProductAmountResponse> getProductAmount() {
		ProductAmountResponse response = service.getProductAmount(getParam());
		return ResponseEntity.ok().body(response);
	}

	private ProductInfoRequest getParam() {
		Long[] couponIds = {1L, 2L};

		ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1L)
			.couponIds(couponIds)
			.build();

		return request;
	}
}
