package antigravity.domain.reader;

import antigravity.common.CouponFailedException;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionReaderImpl implements PromotionReader {
    private final PromotionRepository promotionRepository;


    @Override
    public Promotion getPromotionByPromotionIdAndCouponId(int productId, int couponId) {
        return promotionRepository.findPromotionByPromotionIdAndCouponId(productId, couponId).orElseThrow(
                () -> new CouponFailedException()
        );
    }
}
