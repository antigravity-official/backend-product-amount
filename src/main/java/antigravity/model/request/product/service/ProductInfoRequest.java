package antigravity.model.request.product.service;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductInfoRequest {

	private int productId; //상품 ID
	private Integer[] couponIds; //쿠폰 ID

	public static ProductInfoRequest toDto(final int productId, final List<Integer> couponIds) {
		return ProductInfoRequest.builder()
			.productId(productId)
			.couponIds((couponIds != null) ? couponIds.toArray(new Integer[couponIds.size()]) : null)
			.build();
	}
}
