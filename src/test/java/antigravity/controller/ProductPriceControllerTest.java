package antigravity.controller;

import antigravity.controller.utils.ProductAmountResponseUtil;
import antigravity.controller.utils.ProductInfoRequestUtil;
import antigravity.global.dto.request.ProductInfoRequest;
import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.global.fixture.ProductAmountResponseFixture;
import antigravity.global.fixture.ProductInfoRequestFixture;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static antigravity.global.fixture.ProductInfoRequestFixture.DUPLICATED_PROMOTIONIDS;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Disabled
@WebMvcTest(ProductPriceController.class)
@DisplayName("[Controller] ProductPrice / WebMvcTest")
public class ProductPriceControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/products/amount";

    @Nested
    @DisplayName("[ProductPriceController] 할인 요청 API")
    class discount {

        @Test
        @DisplayName("[Success] 할인 요청에, 할인 결과 응답을 내린다.")
        void successToReturnResponse() throws Exception {
            //given
            ProductInfoRequest request = ProductInfoRequestUtil.request(ProductInfoRequestFixture.VALID);
            ProductAmountResponse response = ProductAmountResponseUtil.request(ProductAmountResponseFixture.VALID);
//            given(productPriceService.applyDiscount(request.getProductId(), request.getPromotionIds()))
//                    .willReturn(response);

            //when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("productId", valueOf(request.getProductId()))
                    .param("promotionIds", collectionToCommaDelimitedString(request.getPromotionIds()));

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("피팅노드상품"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.originPrice").value(158500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.discountAmount").value(54500))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.finalPrice").value(104000))
                    .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("[Exception] 동일한 프로모션 코드가 중복으로 요청되면, 예외를 던진다.")
        void failByDuplicatedPromotionIds() throws Exception {
            //given
            //todo 익셉션 던지는거 연구..
            ProductInfoRequest request = ProductInfoRequestUtil.request(DUPLICATED_PROMOTIONIDS);
//            given(productPriceService.applyDiscount(1, request.getPromotionIds()))
//                    .willThrow(new BusinessException(INVALID_DISCOUNT_PARAMETER));
            //when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("productId", valueOf(request.getProductId()))
                    .param("promotionIds", collectionToCommaDelimitedString(request.getPromotionIds()));

            //then
//            assertThatThrownBy(() -> productPriceService.applyDiscount(1,request.getPromotionIds()))
//                    .isInstanceOf(BusinessException.class)
//                    .hasMessageContaining(INVALID_DISCOUNT_PARAMETER.getMessage());

        }


    }
}
