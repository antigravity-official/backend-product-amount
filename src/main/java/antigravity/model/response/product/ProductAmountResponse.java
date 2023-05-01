package antigravity.model.response.product;

import antigravity.domain.entity.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductAmountResponse {
	private String name; //상품명
	private int originPrice; //상품 기존 가격
	private int discountPrice; //총 할인 금액
	private int finalPrice; //확정 상품 가격

	//할인 적용 없는 경우
	public static ProductAmountResponse toDto(final Product product) {
		return ProductAmountResponse.builder()
			.name(product.getName())
			.originPrice(product.getPrice())
			.finalPrice(product.getPrice())
			.build();
	}

	//할인 적용 있는 경우
	public static ProductAmountResponse toDto(final Product product, final int discountPrice, final int finalPrice) {
		final int productPrice = product.getPrice();
		return ProductAmountResponse.builder()
			.name(product.getName())
			.originPrice(productPrice)
			.discountPrice(discountPrice)
			.finalPrice(finalPrice)
			.build();
	}

}
