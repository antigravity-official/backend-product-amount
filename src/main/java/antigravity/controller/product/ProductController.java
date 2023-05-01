package antigravity.controller.product;

import java.util.List;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Tag(name = "Product API", description = "상품 API")
public class ProductController {

	private final ProductService service;

	@Operation(summary = "프로모션 적용 상품 가격 조회")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "성공", content = {
			@Content(schema = @Schema(implementation = ProductAmountResponse.class))}),
		@ApiResponse(responseCode = "400", description = "[-200] 유효하지 않은 프로모션입니다."),
		@ApiResponse(responseCode = "404", description = "[-100] 해당 상품 정보가 존재하지 않습니다."),
	})
	@GetMapping("/{productId}/amount")
	public ResponseEntity<ProductAmountResponse> getProductAmount(
		@Parameter @PathVariable @NotNull(message = "상품 ID는 필수 값입니다.") @Positive(message = "상품 ID는 양수입니다.") Integer productId,
		@Parameter @RequestParam(required = false) @Size(min = 1, message = "쿠폰 ID는 최소 1개가 필요합니다.")
		List<@NotNull(message = "쿠폰 ID를 입력해주세요.") @Positive(message = "쿠폰 ID는 양수입니다.") Integer> couponIds) {
		log.debug("상품 가격 조회 요청 - productId: {}, couponIds: {}", productId, couponIds);
		return ResponseEntity.ok(service.getProductAmount(ProductInfoRequest.toDto(productId, couponIds)));
	}

}
