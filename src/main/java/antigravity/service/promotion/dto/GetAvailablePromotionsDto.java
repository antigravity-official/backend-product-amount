package antigravity.service.promotion.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetAvailablePromotionsDto {
    private final long productId;
    private final List<Long> promotionIds;
    private final LocalDateTime availableAt;
}
