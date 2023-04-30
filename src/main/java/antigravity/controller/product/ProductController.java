package antigravity.controller.product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService service;

	//상품 가격 조회 API
	@GetMapping("/{productId}/amount")
	public ResponseEntity<ProductAmountResponse> getProductAmount(
		@PathVariable @NotNull(message = "상품 ID는 필수 값입니다.") @Positive(message = "상품 ID는 양수입니다.") Integer productId,
		@RequestParam(required = false) @Size(min = 1, message = "쿠폰 ID는 최소 1개가 필요합니다.") int[] couponIds) {
		log.debug("상품 가격 조회 요청 - productId: {}, couponIds: {}", productId, couponIds);
		return ResponseEntity.ok(service.getProductAmount(ProductInfoRequest.toDto(productId, couponIds)));
	}
	
}
