package antigravity.global;

import antigravity.dto.request.ProductInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ProductInfoRequestFixture {
    VALID(1, List.of(1, 2)),
    INVALID_PRODUCTIDS(-2147483648, List.of(1, 2)),
    INVALID_PROMOTIONIDS(1, List.of(-2147483648, 1)),
    DUPLICATED_PROMOTIONIDS(1, List.of(1, 1)),
    EMPTY_PROMOTIONIDS(1,new ArrayList<>());

    private final int productId;
    private final List<Integer> promotionIds;

    public ProductInfoRequest toEntity() {
        return ProductInfoRequest.of(
                productId,
                promotionIds
        );
    }
}
