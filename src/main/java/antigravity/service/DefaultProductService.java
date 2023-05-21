package antigravity.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.NotFoundResourceException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductJpaRepository;
import antigravity.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Primary
public class DefaultProductService implements ProductService {

	private final ProductJpaRepository productRepository;

	private final PromotionJpaRepository promotionJpaRepository;

	private final static int MIN_TOTAL_AMOUNT = 10000;

	public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
		// System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

		Product product = getProduct(request.productId());
		int discountAmount = 0;
		int totalAmount = product.getPrice();

		for (int i = 0; i < request.couponIds().length; i++) {
			Promotion promotion = getProductPromotion(product.getId(), request.couponIds()[i]);
			discountAmount += promotion.calculateDiscountPrice(product.getPrice());
		}

		totalAmount = Math.max(totalAmount - discountAmount, MIN_TOTAL_AMOUNT);

		return ProductAmountResponse.builder()
			.name(product.getName())
			.originPrice(product.getPrice())
			.discountPrice(discountAmount)
			.finalPrice(totalAmount)
			.build();
	}

	private Product getProduct(long productId) {
		return productRepository.findById(productId)
			.orElseThrow(() -> new NotFoundResourceException(Product.class));
	}

	private Promotion getProductPromotion(long productId, long couponId) {
		return promotionJpaRepository.findByIdAndProductId(productId, couponId)
			.orElseThrow(() -> new NotFoundResourceException(PromotionProducts.class));
	}
}
