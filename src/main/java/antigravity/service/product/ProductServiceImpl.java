package antigravity.service.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.product.Product;
import antigravity.exception.product.ProductNotFoundException;
import antigravity.model.request.product.service.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;
import antigravity.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;

	/**
	 * 상품 가격 조회
	 * @param request 조회할 상품 정보
	 * @return ProductAmountResponse 상품 가격 정보
	 */
	@Transactional(readOnly = true)
	public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
		final Product product = getProductById(request.getProductId());
		log.debug("상품 가격 조회 - 상품: {}", product);
		return null;
	}

	/**
	 * 상품 ID로 상품 정보 조회
	 * @param productId 상품 ID
	 * @return Product 상품 정보
	 */
	@Transactional(readOnly = true)
	public Product getProductById(final Integer productId) {
		return repository.findById(productId).orElseThrow(ProductNotFoundException::new);
	}
}
