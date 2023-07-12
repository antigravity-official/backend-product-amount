package antigravity.controller.utils;

import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.global.fixture.ProductAmountResponseFixture;

public abstract class ProductAmountResponseUtil {

    public static ProductAmountResponse request(ProductAmountResponseFixture fixture) {
        return ProductAmountResponse.of(fixture.getName(), fixture.getOriginPrice(), fixture.getDiscountAmount(), fixture.getFinalPrice());
    }
}
