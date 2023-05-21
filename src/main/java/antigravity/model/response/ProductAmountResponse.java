package antigravity.model.response;

import lombok.Builder;

/**
 * @param name          상품명
 * @param originPrice   상품 기존 가격
 * @param discountPrice 총 할인 금액
 * @param finalPrice    확정 상품 가격
 */
@Builder
public record ProductAmountResponse(
	String name,
	int originPrice,
	int discountPrice,
	int finalPrice
) {
}
