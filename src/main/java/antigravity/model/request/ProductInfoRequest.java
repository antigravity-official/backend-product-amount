package antigravity.model.request;

import lombok.Builder;
import lombok.Data;

/**
 * @param productId 상품 id
 * @param couponIds 상품 쿠폰 id
 */
@Builder
public record ProductInfoRequest(
	long productId,
	long[] couponIds
) {
}
