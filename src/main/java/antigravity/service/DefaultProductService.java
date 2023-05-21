package antigravity.service;

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
		int discountPrice = 0;

		for (int i = 0; i < request.couponIds().length; i++) {
			Promotion promotion = promotionService.getByIdAndProductId(product.getId(), request.couponIds()[i]);
			discountPrice += promotion.calculateDiscountPrice(product.getPrice());
		}

		int totalPrice = calculateTotalPrice(product.getPrice(), discountPrice);

		return ProductAmountResponse.builder()
			.name(product.getName())
			.originPrice(product.getPrice())
			.discountPrice(product.getPrice() - totalPrice)
			.finalPrice(totalPrice)
			.build();
	}

	private Product getProduct(long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundResourceException(Product.class));
	}

	private Integer calculateTotalPrice(int productPrice, int discountPrice) {
		int totalPrice = Math.max(productPrice - discountPrice, MIN_TOTAL_AMOUNT);
		return (totalPrice / 1000) * 1000;
	}
}
