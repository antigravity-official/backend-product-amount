package antigravity.service.promotion;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.product.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static antigravity.error.ErrorCode.NOT_APPLICABLE_SELECTED_PRODUCT;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;

@Disabled
@DisplayName("[Service] PromotionService - SpringBootTest")
public class PromotionServiceTest extends ServiceTestSupport {

    @MockBean
    private ProductPriceService productPriceService;

    @MockBean
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Nested
    @DisplayName("[findValidPromotion] 유효한 프로모션을 찾고, 유효하지 않다면 예외를 던진다.")
    class ApplyDiscountTest {

        Product product;
        Promotion promotion1;
        Promotion promotion2;

        @BeforeEach
        void initSetting() {
            promotion1 = VALID_PROMOTION1.toEntity();
            promotion2 = VALID_PROMOTION2.toEntity();
        }

        @Test
        @DisplayName("[Exception] 해당 상품에 매핑되지 않은 프로모션을 사용하려 할 경우 예외를 던진다.")
        void failByNonMappingPromotion() throws Exception {
            //given - @BeforeEach
            //when
            String ExpectedErrorMessage = NOT_APPLICABLE_SELECTED_PRODUCT.getMessage();


            //            given(promotionService.findValidPromotions(any(),any()))
//                    .willThrow(BusinessException.class);

        }

        @Test
        @DisplayName("[Exception] 해당 상품에 매핑은 되어 있지만, 존재하지 않는 프로모션일 경우 예외를 던진다.")
        void failByNonexistentPromotion() throws Exception {
        }

        @Test
        @DisplayName("[Exception] 기간이 잘못 지정된 프로모션일 경우 예외를 던진다.")
        void failByInvalidPeriodPromotion() throws Exception {

        }
    }
}
