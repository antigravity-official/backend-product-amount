package antigravity.model.response.product;

import antigravity.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ProductAmountResponse {

	@Schema(description = "상품명", example = "더핏브라")
	private String name;

	@Schema(description = "상품 기존 가격", example = "43000")
	private int originPrice;

	@Schema(description = "총 할인 금액", example = "9100")
	private int discountPrice;

	@Schema(description = "확정 상품 가격", example = "33900")
	private int finalPrice;

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
