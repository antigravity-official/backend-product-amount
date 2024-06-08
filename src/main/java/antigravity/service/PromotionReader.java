package antigravity.service;

import antigravity.domain.ProductWithPromotions;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PromotionReader {
    private final PromotionRepository promotionRepository;

    public ProductWithPromotions getAppliedPromotionOf(Product product) {
        List<Promotion> promotions = promotionRepository.findValidPromotionsByProductId(product.getId());
        return ProductWithPromotions.builder()
                .product(product)
                .promotions(promotions)
                .build();
    }
}
