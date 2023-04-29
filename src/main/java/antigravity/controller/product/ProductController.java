package antigravity.controller.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antigravity.model.request.product.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.service.product.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService service;

	//상품 가격 추출 api
	@GetMapping("/amount")
	public ResponseEntity<ProductAmountResponse> getProductAmount() {
		return ResponseEntity.ok(service.getProductAmount(getParam()));
	}

	private ProductInfoRequest getParam() {
		int[] couponIds = {1, 2};

		ProductInfoRequest request = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(couponIds)
			.build();

		return request;
	}
}
