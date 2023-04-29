package antigravity.service.product;

import org.springframework.stereotype.Service;

import antigravity.model.request.product.ProductInfoRequest;
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
	public ProductAmountResponse getProductAmount(final ProductInfoRequest request) {
		final int productId = request.getProductId();

		return null;
	}
}
