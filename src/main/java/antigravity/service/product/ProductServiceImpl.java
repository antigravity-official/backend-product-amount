package antigravity.service.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion_products.PromotionProducts;
import antigravity.exception.product.ProductNotFoundException;
import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion_products.PromotionProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepo;
	private final PromotionProductsRepository promotionProductRepo;

	/**
	 * 상품 가격 조회
	 * @param request 조회할 상품 정보
	 * @return ProductAmountResponse 상품 가격 정보
	 */
	@Transactional(readOnly = true)
	public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
		final Product product = getProductById(request.getProductId());
		//해당 쿠폰 ID의 프로모션이면서 해당 상품에 적용되는 프로모션 정보만 조회
		final List<PromotionProducts> promotionProductsList
			= promotionProductRepo.findAllWithPromotionByProductAndCouponIds(product, request.getCouponIds());
		log.debug("적용 대상 상품 프로모션: {}", promotionProductsList);
		if (promotionProductsList.isEmpty()) { //case 1: 적용할 프로모션 미존재
			return ProductAmountResponse.toDto(product);
		}
		//case 2: 적용할 프로모션 존재
		return null;
	}

	/**
	 * 상품 ID로 상품 정보 조회
	 * @param productId 상품 ID
	 * @return Product 상품 정보
	 */
	@Transactional(readOnly = true)
	public Product getProductById(final Integer productId) {
		log.debug("상품 조회 요청 - productId: {}", productId);
		final Product product = productRepo.findById(productId).orElseThrow(ProductNotFoundException::new);
		log.debug("상품 조회 완료 - product: {}", product);
		return product;
	}
}
