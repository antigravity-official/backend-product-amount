package antigravity.utils;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.request.ProductInfoWrapperRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Utility class for mapping ProductInfoRequest to ProductInfoWrapperRequest.
 */
public class ProductInfoMapper {

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
}
