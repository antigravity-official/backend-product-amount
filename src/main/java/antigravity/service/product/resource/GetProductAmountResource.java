package antigravity.service.product.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class GetProductAmountResource {
    private final String name;
    private final long originPrice;
    private final long discountPrice;
    private final long finalPrice;
}
