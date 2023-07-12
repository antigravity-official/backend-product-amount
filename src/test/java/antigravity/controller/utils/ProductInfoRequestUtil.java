package antigravity.controller.utils;

import antigravity.global.dto.request.ProductInfoRequest;
import antigravity.global.fixture.ProductInfoRequestFixture;

public abstract class ProductInfoRequestUtil {

    public static ProductInfoRequest request(ProductInfoRequestFixture fixture) {
        return ProductInfoRequest.of(fixture.getProductId(), fixture.getPromotionIds());
    }
}
