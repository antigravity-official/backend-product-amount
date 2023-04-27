package antigravity.rds.mapper;


import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductMapper {
    Optional<ProductAmountResponse> price(ProductInfoRequest productInfoRequest);
}

