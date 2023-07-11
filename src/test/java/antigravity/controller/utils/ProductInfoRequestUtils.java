package antigravity.controller.utils;

import antigravity.dto.request.ProductInfoRequest;
import antigravity.global.ProductInfoRequestFixture;

public abstract class ProductInfoRequestUtils {

    public static ProductInfoRequest request(ProductInfoRequestFixture fixture) {
        return ProductInfoRequest.of(fixture.getProductId(), fixture.getPromotionIds());
    }
}
