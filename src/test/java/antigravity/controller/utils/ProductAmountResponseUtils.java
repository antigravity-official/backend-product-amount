package antigravity.controller.utils;

import antigravity.dto.response.ProductAmountResponse;
import antigravity.global.ProductAmountResponseFixture;

public abstract class ProductAmountResponseUtils {

    public static ProductAmountResponse request(ProductAmountResponseFixture fixture) {
        return ProductAmountResponse.of(fixture.getName(), fixture.getOriginPrice(), fixture.getDiscountAmount(), fixture.getFinalPrice());
    }
}
