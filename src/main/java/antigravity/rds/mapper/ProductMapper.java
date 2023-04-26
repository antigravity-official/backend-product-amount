package antigravity.rds.mapper;


import antigravity.model.response.ProductAmountResponse;
import antigravity.rds.entity.Promotion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductMapper {
    Optional<ProductAmountResponse> price(Integer id, int[] couponIds);
}

