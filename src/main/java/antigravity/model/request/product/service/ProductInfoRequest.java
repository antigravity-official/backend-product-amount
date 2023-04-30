package antigravity.model.request.product.service;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductInfoRequest {

	private int productId; //상품 ID
	private int[] couponIds; //쿠폰 ID

	public static ProductInfoRequest toDto(final int productId, final int[] couponIds) {
		return ProductInfoRequest.builder()
			.productId(productId)
			.couponIds(couponIds)
			.build();
	}
}
