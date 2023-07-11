package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.promotion.PromotionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static antigravity.error.ErrorCode.DUPLICATED_PROMOTION_USAGE;
import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


/**
 * todo Mock Injection Complete
 */
@Disabled
@DisplayName("[Service] ProductPriceService - SpringBootTest")
public class ProductPriceServiceTest extends ServiceTestSupport{

    @MockBean
    private PromotionService promotionService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ProductPriceService productPriceService;

    @Nested
    @DisplayName("[applyDiscount] 할인 계산 응답을 내리고, 이상값에 예외를 던진다.")
    class ApplyDiscountTest {

        Product product;
        Promotion promotion1;
        Promotion promotion2;

        @BeforeEach
        void initSetting() {
            product = VALID_PRODUCT1.toEntity();
            promotion1 = VALID_PROMOTION1.toEntity();
            promotion2 = VALID_PROMOTION2.toEntity();
            given(productQueryRepo.findById(any())).willReturn(ofNullable(product));
            given(productService.findProductById(any())).willReturn(product);
            given(promotionService.findPromotionsByProductId(any(), any())).willReturn( List.of(promotion1, promotion2));

        }

        @Test
        @DisplayName("[Success] 입력된 프로모션이 없을 때, 할인하지 않은 원본 가격을 리턴한다.")
        void successToReturnWhenEmptyPromotion() throws Exception {
            //given - @BeforeEach
            //when & then
            ProductAmountResponse expectedResponse = productPriceService.applyDiscount(1, emptyList());
            assertAll(
                    () -> assertThat(expectedResponse.getName()).isEqualTo(product.getName()),
                    () -> assertThat(expectedResponse.getOriginPrice()).isEqualTo(product.getPrice()),
                    () -> assertThat(expectedResponse.getDiscountAmount()).isZero(),
                    () -> assertThat(expectedResponse.getFinalPrice()).isEqualTo(product.getPrice())
            );
        }

        @Test
        @DisplayName("[Success] 유효한 할인 프로모션일 경우 계산 후 응답을 내린다.")
        void  successToReturnValidPromotion() throws Exception {
            //given - @BeforeEach
            given(promotionService.verifyDuplicatePromotionUsage(any())).willReturn(true);

            //when
            final int expectedDiscountedValue = promotion1.getDiscountValue() + (product.getPrice() / 100 * promotion2.getDiscountValue());
            final int finalExpectedDiscountedPrice = product.getPrice() - expectedDiscountedValue;
            final int remainingPrice =  finalExpectedDiscountedPrice % 1000;
            ProductAmountResponse expectedResponse = productPriceService.applyDiscount(1, List.of(1, 2));

            //then
            assertAll(
                    () -> assertThat(expectedResponse.getName()).isEqualTo(product.getName()),
                    () -> assertThat(expectedResponse.getOriginPrice()).isEqualTo(product.getPrice()),
                    () -> assertThat(expectedResponse.getDiscountAmount()).isEqualTo(expectedDiscountedValue + remainingPrice),
                    () -> assertThat(expectedResponse.getFinalPrice()).isEqualTo(finalExpectedDiscountedPrice - remainingPrice)
            );
        }

        @Test
        @DisplayName("[Exception] 동일한 프로모션을 중복 사용할 경우 예외를 던진다.")
        void  failByReturnValidPromotion() throws Exception {
            //given - @BeforeEach
            given(promotionService.verifyDuplicatePromotionUsage(any())).willReturn(false);
            String expectedErrorMessage = DUPLICATED_PROMOTION_USAGE.getMessage();
            //when && then
            //then
            assertThatThrownBy(() -> productPriceService.applyDiscount(1, List.of(1, 1)))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(expectedErrorMessage);
        }
    }
}
