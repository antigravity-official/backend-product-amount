package antigravity.service;

import antigravity.domain.entity.Promotion;
import antigravity.error.BusinessException;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static antigravity.error.ErrorCode.INVALID_COUPON;
import static antigravity.error.ErrorCode.NOT_EXIST_COUPON;

@RequiredArgsConstructor
@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public void isPromotionValid(List<Promotion> promotions) {
        Date currentDate = new Date();
        for (Promotion promotion : promotions) {
            if (!isPromotionWithinValidPeriod(promotion.getUseStartedAt(), promotion.getUseEndedAt(), currentDate)) {
                throw new BusinessException(INVALID_COUPON);
            }
        }
    }

    private boolean isPromotionWithinValidPeriod(Date startDate, Date endDate, Date currentDate) {
        return currentDate.after(startDate) && currentDate.before(endDate);
    }

    public List<Promotion> findByPromotionIds(int[] promotionIds) {
        List<Promotion> promotions = new ArrayList<>();

        for (int promotionId : promotionIds) {
            promotions.add(findByPromotionId(promotionId));
        }
        return promotions;
    }

    public Promotion findByPromotionId(Integer promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new BusinessException(NOT_EXIST_COUPON));
    }
}
