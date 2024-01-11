package antigravity.utils;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.request.ProductInfoWrapperRequest;
import antigravity.model.response.ProductAmountResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for mapping Request and Responses.
 */
public class RequestResponseMapper {

    /**
     * Maps a ProductInfoRequest to a ProductInfoWrapperRequest.
     *
     * @param request The ProductInfoRequest to be mapped.
     * @return A ProductInfoWrapperRequest containing the mapped data.
     */
    public static ProductInfoWrapperRequest mapToWrapperRequest(ProductInfoRequest request) {
        return ProductInfoWrapperRequest.builder()
                .productId(request.getProductId())
                .couponIds(Arrays.stream(request.getCouponIds())
                        .boxed()
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Builds a response containing product pricing information after applying discounts.
     *
     * @param product The product for which the response is being built.
     * @param discount The discount amount to be applied.
     * @return ProductAmountResponse with the calculated pricing details.
     */
    public static ProductAmountResponse mapToAmountResponse(Product product, int discount) {
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discount)
                .finalPrice(roundFinalPrice(product.getPrice(), discount))
                .build();
    }

    /**
     * Rounds the final price to the nearest thousand after applying the discount.
     *
     * @param finalPrice The price before rounding.
     * @param discount The discount amount to be subtracted from the final price.
     * @return The rounded final price.
     */
    private static int roundFinalPrice(int finalPrice, int discount) {
        BigDecimal roundedFinalPrice = BigDecimal
                .valueOf(finalPrice)
                .subtract(BigDecimal.valueOf(discount))
                .divide(BigDecimal.valueOf(1000), 0, RoundingMode.UP)
                .multiply(BigDecimal.valueOf(1000));

        return roundedFinalPrice.intValue();
    }
}
