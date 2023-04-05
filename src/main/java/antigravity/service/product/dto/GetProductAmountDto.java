package antigravity.service.product.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetProductAmountDto {
    private final long productId;
    private final List<Long> couponIds;
    private final LocalDateTime requestAt;
}
