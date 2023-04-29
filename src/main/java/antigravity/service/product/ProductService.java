package antigravity.service.product;

import antigravity.model.request.product.ProductInfoRequest;
import antigravity.model.response.product.ProductAmountResponse;

public interface ProductService {

	ProductAmountResponse getProductAmount(ProductInfoRequest request);

}
