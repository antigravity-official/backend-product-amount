package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.global.ProductAmountResponseFixture;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.DiscountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;
import static java.util.List.of;

@SpringBootTest(classes = ProductPriceService.class)
@DisplayName("[Service] ProductPriceService - Service Layer Slice Test")
public class ProductPriceServiceTest extends ServiceTestSupport {

    @MockBean
    DiscountService discountService;

    @Autowired
    ProductPriceService productPriceService;

    @Nested
    @DisplayName("[applyDiscount] 할인 요청을 정상적으로 내립니다.")
    class ApplyDiscountTest {

        Product product = VALID_PRODUCT1.toEntity();
        List<Promotion> promotions = of(VALID_PROMOTION1.toEntity(), VALID_PROMOTION2.toEntity());
        ProductAmountResponse expectedResponse = ProductAmountResponseFixture.VALID.toEntity();

        @Test
        void should_Success_When_isPromotionPresent() throws Exception {
            //given - @BeforeEach
//            given(prod
            //when && then

        }
    }
}
