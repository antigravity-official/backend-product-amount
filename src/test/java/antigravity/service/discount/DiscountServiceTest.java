package antigravity.service.discount;

import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
@DisplayName("[Service] DiscountService - SpringBootTest")
public class DiscountServiceTest extends ServiceTestSupport {

    @Autowired
    private DiscountService discountService;

    @Nested
    @DisplayName("[getFinalDiscountedPrice] 최종 할인가 최소/최대 값 검증 로직")
    class FinalDiscountedPriceTest {
    }
}
