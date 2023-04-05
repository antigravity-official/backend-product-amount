package antigravity.service.product.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetProductAmountResource {
    private final String name;
    private final long originPrice;
    private final long discountPrice;
    private final long finalPrice;
}
