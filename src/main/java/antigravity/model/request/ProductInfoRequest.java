package antigravity.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

/**
 * @param productId 상품 id
 * @param couponIds 상품 쿠폰 id
 */
@Builder
public record ProductInfoRequest(
	@NotNull
	Long productId,
	@NotEmpty
	Long[] couponIds
) {
}
