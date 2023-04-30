package antigravity.util.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import antigravity.domain.entity.product.Product;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PriceUtil { //상품 가격 관련

	@Value("${product.price.min}")
	private int minimumProductPrice; //최소 상품 금액

	/**
	 * 최소 상품 금액보다 미만인 경우, 최소 상품 금액에 맞도록 할인 금액을 조정
	 * @param product 상품 정보
	 * @param discountAmount 총 할인 금액
	 * @return 조정된 총 할인 금액
	 */
	public int adjustDiscountAmount(final Product product, final int discountAmount) {
		final int productPrice = product.getPrice();
		if ((productPrice - discountAmount) < minimumProductPrice) { //상품 가격에서 총 할인 금액을 뺀 금액이 최소 상품 금액보다 작은 경우
			final int adjustedDiscountAmount = productPrice - minimumProductPrice; //조정된 총 할인 금액
			log.debug("총 할인 금액 조정 - 조정된 총 할인 금액: {}, 상품: {}, 기존 총 할인 금액: {}"
				, adjustedDiscountAmount, product, discountAmount);
			return adjustedDiscountAmount;
		}
		return discountAmount; //총 할인 금액 조정 필요없는 경우
	}

}
