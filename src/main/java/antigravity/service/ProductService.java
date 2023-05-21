package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

public interface ProductService {

	ProductAmountResponse getProductAmount(ProductInfoRequest request);
}
