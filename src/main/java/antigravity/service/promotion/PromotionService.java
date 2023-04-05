package antigravity.service.promotion;

import antigravity.domain.promotion.Promotion;
import antigravity.domain.promotion.PromotionRepository;
import antigravity.service.promotion.dto.GetAvailablePromotionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    // 필요에 따라 cache 적용
    @Transactional(readOnly = true)
    public List<Promotion> getAvailablePromotions(GetAvailablePromotionsDto dto) {
        return promotionRepository.findByProductIdAndPromotionIdsAndUseAtBetween(
                dto.getProductId(),
                dto.getPromotionIds(),
                dto.getAvailableAt()
        );
    }
}
