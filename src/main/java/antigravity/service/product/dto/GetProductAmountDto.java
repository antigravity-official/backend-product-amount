package antigravity.service.product.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@EqualsAndHashCode
public class GetProductAmountDto {
    private long productId;
    private List<Long> couponIds;
    private LocalDateTime requestAt;

    public GetProductAmountDto(long productId, List<Long> couponIds, LocalDateTime requestAt) {
        setProductId(productId);
        setCouponIds(couponIds);
        setRequestAt(requestAt);
    }

    private void setProductId(long productId) {
        this.productId = productId;
    }

    private void setCouponIds(List<Long> couponIds) {
        if(couponIds == null) {
            throw new IllegalArgumentException("couponIds must not be null");
        }
        this.couponIds = couponIds;
    }

    private void setRequestAt(LocalDateTime requestAt) {
        if(requestAt == null) {
            throw new IllegalArgumentException("requestAt must not be null");
        }
        this.requestAt = requestAt;
    }
}
