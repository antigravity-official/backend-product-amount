package antigravity.controller;

import antigravity.controller.utils.ProductInfoRequestUtil;
import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.base.ControllerTestSupport;
import antigravity.global.dto.request.ProductInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static antigravity.global.fixture.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.fixture.ProductInfoRequestFixture.VALID;
import static antigravity.global.fixture.PromotionFixture.VALID_FIX_PROMOTION1;
import static antigravity.global.fixture.PromotionFixture.VALID_RATE_PROMOTION2;
import static java.lang.String.valueOf;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductPriceController.class)
@DisplayName("[Controller] ProductPriceController - WebMvcTest")
class ProductPriceControllerTest extends ControllerTestSupport {

    @InjectMocks
    private ProductPriceController productPriceController;

    private static final String BASE_URL = "/products/amount";

    @Nested
    @DisplayName("[ProductPriceController] 할인 요청 API")
    class TestGetProductAmount {

        @Test
        @DisplayName("[Success] 할인 요청에, 할인 결과 응답을 내린다.")
        void When_ValidRequested_Then_ReturnResponse() throws Exception {
            //given
            ProductInfoRequest request = ProductInfoRequestUtil.request(VALID);
            Product product = VALID_PRODUCT1.toEntity();
            List<Integer> productIds = List.of(-1, -2);
            Promotion promotion1 = VALID_FIX_PROMOTION1.toEntity();
            Promotion promotion2 = VALID_RATE_PROMOTION2.toEntity();
            List<Promotion> promotions = List.of(promotion1, promotion2);

//
//            doReturn(product).when(productService).findProductById(any());
//            doReturn(productIds).when(promotionService).findMappedPromotionIdsByProductId(any());
//            doReturn(productIds).when(promotionService.findApplicablePromotions(any(), any()));
//            doReturn(promotions).when(promotionService).findAllPromotionsByIds(any());


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
    }
}
