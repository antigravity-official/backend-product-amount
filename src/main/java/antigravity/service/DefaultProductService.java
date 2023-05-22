package antigravity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotFoundResourceException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DefaultProductService implements ProductService {

	private final ProductRepository productRepository;

	private final PromotionService promotionService;

	private final static int MIN_TOTAL_AMOUNT = 10000;

	public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
		// System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

		Product product = getProduct(request.productId());

		List<Promotion> promotions = promotionService.findAllByIdsAndProductId(request.couponIds(), request.productId());
		int discountPrice = promotions.stream().mapToInt(p -> p.calculateDiscountPrice(product.getPrice())).sum(); // 총 할인 금액 계산
		int totalPrice = calculateTotalPrice(product.getPrice(), discountPrice);

		return ProductAmountResponse.builder()
			.name(product.getName())
			.originPrice(product.getPrice())
			.discountPrice(product.getPrice() - totalPrice) // 할인 가격 = 최종 가격 - 기존 가격
			.finalPrice(totalPrice)
			.build();
	}

	private Product getProduct(long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundResourceException(Product.class)); // 올바른 id가 아닐 경우 예외 발생
	}

	private Integer calculateTotalPrice(int productPrice, int discountPrice) {
		int totalPrice = Math.max(productPrice - discountPrice, MIN_TOTAL_AMOUNT); // 최소값 이하일 경우 최소값 반환
		return (totalPrice / 1000) * 1000; // 1000단위 절삭
	}
}
