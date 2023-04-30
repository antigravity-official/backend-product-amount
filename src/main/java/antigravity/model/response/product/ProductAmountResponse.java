package antigravity.model.response.product;

import antigravity.domain.entity.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductAmountResponse {
	private Integer id; //상품 ID
	private String name; //상품명
	private int originPrice; //상품 기존 가격
	private int discountPrice; //총 할인 금액
	private int finalPrice; //확정 상품 가격

	//할인 적용 없는 경우
	public static ProductAmountResponse toDto(final Product product) {
		return ProductAmountResponse.builder()
			.id(product.getId())
			.name(product.getName())
			.originPrice(product.getPrice())
			.finalPrice(product.getPrice())
			.build();
	}

}
