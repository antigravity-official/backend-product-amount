package antigravity.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class GetProductAmountDto {
    private final long productId;
    private final List<Long> couponIds;
}
