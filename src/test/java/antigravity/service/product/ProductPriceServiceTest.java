package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.promotion.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;

@Disabled
@DisplayName("[Service] ProductPriceService - Mock Service Layer Test")
public class ProductPriceServiceTest extends ServiceTestSupport {

    @InjectMocks
    private ProductPriceService productPriceService;

    @Nested
    @DisplayName("[determineToApplyTest] productIds 요청 여부에 따라 할인 여부를 결정합니다.")
    class ApplyDiscountTest {

        Product product;
        Promotion fixPromotion;
        Promotion ratePromotion;

        @BeforeEach
        void initSetting() {
            product = VALID_PRODUCT1.toEntity();
            fixPromotion = VALID_PROMOTION1.toEntity();;
            ratePromotion = VALID_PROMOTION2.toEntity();;
        }


    }
