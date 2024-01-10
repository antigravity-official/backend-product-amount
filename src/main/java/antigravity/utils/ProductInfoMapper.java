package antigravity.utils;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.request.ProductInfoWrapperRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ProductInfoMapper {

    public static ProductInfoWrapperRequest mapToWrapperRequest(ProductInfoRequest request) {
        return ProductInfoWrapperRequest.builder()
                .productId(request.getProductId())
                .couponIds(Arrays.stream(request.getCouponIds())
                        .boxed()
                        .collect(Collectors.toList()))
                .build();
    }
}
