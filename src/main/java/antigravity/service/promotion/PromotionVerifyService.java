package antigravity.service.promotion;

import antigravity.domain.Promotion;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromotionVerifyService {

    public boolean isEachPromotionHasValidExpirationDate(List<Promotion> promotions) {
        LocalDate currentDate = LocalDate.now();
        return promotions.stream()
                .allMatch(promotion -> isPromotionHasValidExpirationDate(
                        promotion.getUseStartedAt(),
                        promotion.getUseEndedAt(),
                        currentDate
                ));
    }

    private boolean isPromotionHasValidExpirationDate(LocalDate startDate, LocalDate endDate, LocalDate currentDate) {
        return !currentDate.isAfter(endDate) && !currentDate.isBefore(startDate);
    }

    public boolean isPromotionRequestExistence(List<Integer> promotionIds) {
        return !promotionIds.isEmpty();
    }
}
