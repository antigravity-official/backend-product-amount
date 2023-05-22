package antigravity.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Data;

/**
 * @param productId 상품 id
 * @param couponIds 상품 쿠폰 id
 */
@Builder
public record ProductInfoRequest(
	@NotNull(message = "상품 id가 입력되지않았습니다.")
	@Positive(message = "상품 id는 음수일 수 없습니다.")
	Long productId,
	@NotEmpty(message = "쿠폰 id가 입력되지않았습니다.")
	Long[] couponIds
) {
}
