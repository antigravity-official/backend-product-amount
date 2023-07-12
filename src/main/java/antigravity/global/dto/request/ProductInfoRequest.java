package antigravity.global.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Getter
public class ProductInfoRequest {
    private int productId;
    private List<Integer> promotionIds;

    /* static factory method */
    public static ProductInfoRequest of(
            final int productId,
            final List<Integer> promotionIds
    ) {
        return ProductInfoRequest.builder()
                .productId(productId)
                .promotionIds(promotionIds)
                .build();
    }
}
