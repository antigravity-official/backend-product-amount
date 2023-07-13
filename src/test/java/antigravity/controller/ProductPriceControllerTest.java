package antigravity.controller;

import antigravity.global.base.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static antigravity.domain.PromotionProducts.of;
import static antigravity.global.fixture.ProductFixture.*;
import static antigravity.global.fixture.PromotionFixture.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("[Controller] ProductPriceController - SpringBoot Integration Test")
class ProductPriceControllerTest extends ControllerTestSupport {

    private final String BASE_URL = "/products/amount";
    private final int INVALID = -2147483648;
    private int product1Id;
    private int promotion1Id;
    private int promotion2Id;
    private int promotion3Id;

    @Nested
    @DisplayName("[ProductPriceController] 할인 요청 API")
    class TestGetProductAmount {

        @BeforeEach
        void initSetting() {
            product1Id = productRepository.save(VALID_PRODUCT1.toEntity()).getId();
            promotion1Id = promotionRepository.save(VALID_FIX_PROMOTION1.toEntity()).getId();
            promotion2Id = promotionRepository.save(VALID_RATE_PROMOTION2.toEntity()).getId();
            promotion3Id = promotionRepository.save(VALID_PROMOTION3.toEntity()).getId();

            promotionProductsRepository.save(of(promotion1Id, product1Id));
            promotionProductsRepository.save(of(promotion2Id, product1Id));
            promotionProductsRepository.save(of(INVALID, product1Id));
        }

        @Test
        @DisplayName("[Success] 유효한 할인 요청에, 할인 결과 응답을 내린다.")
        void When_ValidRequested_Expect_ReturnResponse() throws Exception {
            //given - @BeforeEach
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, promotion2Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("남성 보정 속옷"))
                    .andExpect(jsonPath("$.originPrice").value(158500))
                    .andExpect(jsonPath("$.discountAmount").value(54500))
                    .andExpect(jsonPath("$.finalPrice").value(104000))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 조회하려는 상품이, 상품 테이블에 존재하지 않는다면 예외를 던진다. [P001]")
        void When_NonexistenceProductRequested_Expect_ThrowExceptionP001() throws Exception {
            //given - @BeforeEach
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    INVALID, List.of(promotion1Id, promotion2Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isNotFound())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P001"))
                    .andDo(print());
        }


        @Test
        @DisplayName("[Exception] 매핑은 이루어졌으나, 해당 매핑이 존재하지 않는 프로모션을 참조하고, 그 프로모션이 요청되면 예외를 던진다. [P002]")
        void When_NonexistenceProductRequested_Expect_ThrowExceptionP002() throws Exception {
            //given - @BeforeEach
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, INVALID) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isNotFound())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P002"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 해당 상품에 매핑되지 않은 프로모션이 요청되면 예외를 던진다. [P002]")
        void When_NotMappedPromotionRequested_Expect_ThrowExceptionP002() throws Exception {
            //given - @BeforeEach

            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, promotion3Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isNotFound())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P002"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 상품에 매핑된 단 한 개라도 유효기간이 만료되었다면 예외를 던진다. [P003]")
        void When_ExpiredPromotionRequested_Expect_ThrowExceptionP003() throws Exception {
            //given - @BeforeEach
            int expiredPromotionId = promotionRepository.save(EXPIRED_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(expiredPromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, expiredPromotionId) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P003"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 상품에 매핑된 단 한 개라도 유효기간이 도래하지 않았다면 예외를 던진다. [P003]")
        void When_BeforeUsagePeriodPromotionRequested_Expect_ThrowExceptionP003() throws Exception {
            //given - @BeforeEach
            int beforeUsagePeriodPromotionId = promotionRepository.save(BEFORE_USAGE_PERIOD_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(beforeUsagePeriodPromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, beforeUsagePeriodPromotionId) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P003"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] `WON`, `PERCENT`를 제외한 다른 프로모션 타입이 매핑되어 사용하려고 할 경우 예외를 던진다. [P004]")
        void When_InvalidPromotionTypeRequested_Expect_ThrowExceptionP003() throws Exception {
            //given - @BeforeEach
            int invalidTypePromotionId = promotionRepository.save(UNKNOWN_TYPE_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(invalidTypePromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(promotion1Id, invalidTypePromotionId) ,BASE_URL);
            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P004"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 최종 가격(할인가 및 프로모션 적용가)이 상한 초과라면 예외를 던진다. [P005]")
        void When_FinalDiscountedPriceExceedsUpperLimit_Expect_ThrowExceptionP005() throws Exception {
            //given - @BeforeEach
            int highValueProductId = productRepository.save(HIGH_PRICE_PRODUCT.toEntity()).getId();
            promotionProductsRepository.save(of(promotion1Id, highValueProductId));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    highValueProductId, List.of(promotion1Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P005"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 최종 가격(할인가 및 프로모션 적용가)이 하한 미만이면 예외를 던진다. [P006]")
        void When_FinalDiscountedPriceBelowLowerLimit_Expect_ThrowExceptionP006() throws Exception {
            //given - @BeforeEach
            int lowerValueProduct = productRepository.save(LOW_PRICE_PRODUCT.toEntity()).getId();
            promotionProductsRepository.save(of(promotion1Id, lowerValueProduct));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    lowerValueProduct, List.of(promotion1Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P006"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 정률할인 프로모션의 discountAmount가 음수라면 예외를 던진다. [P007]")
        void When_IsRatePromotionTypeDiscountAmountNegativeInt_Expect_ThrowExceptionP007() throws Exception {
            //given - @BeforeEach
            int minusRatePromotionId = promotionRepository.save(MINUS_RATE_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(minusRatePromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(minusRatePromotionId) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P007"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 정률할인의 프로모션의 discountAmount가 100를 초과한다면 예외를 던진다. [P007]")
        void When_IsRatePromotionTypeDiscountAmountUpperThanHundred_Expect_ThrowExceptionP007() throws Exception {
            //given - @BeforeEach
            int tooHighRatePromotion = promotionRepository.save(SUPER_UPPER_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(tooHighRatePromotion, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(tooHighRatePromotion) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P007"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 정률할인의 프로모션의 discountAmount가 0이라면 예외를 던진다. [P007]")
        void When_IsRatePromotionTypeDiscountAmountZero_Expect_ThrowExceptionP007() throws Exception {
            //given - @BeforeEach
            int zeroPromotionId = promotionRepository.save(ZERO_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(zeroPromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(zeroPromotionId) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P007"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 정액할인 프로모션의 discountAmount가 음수라면 예외를 던진다. [P007]")
        void When_IsFixPromotionTypeDiscountAmountNegativeInt_Expect_ThrowExceptionP007() throws Exception {
            //given - @BeforeEach
            int zeroPromotionId = promotionRepository.save(MINUS_FIX_PROMOTION.toEntity()).getId();
            promotionProductsRepository.save(of(zeroPromotionId, product1Id));
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    product1Id, List.of(zeroPromotionId) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P007"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 조회하려는 상품에 어떠한 프로모션도 적용이 되어있지 않다면 예외를 던진다. [P008]")
        void When_AnyPromotionsNotMatched_Expect_ThrowExceptionP008() throws Exception {
            //given - @BeforeEach
            int validProductId = productRepository.save(VALID_PRODUCT2.toEntity()).getId();
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    validProductId, List.of(promotion1Id) ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P008"))
                    .andDo(print());
        }

        @Test
        @DisplayName("[Exception] 어떠한 프로모션도 요청되지 않았다면 예외를 던진다. [P009]")
        void When_PromotionIdsNotRequested_Expect_ThrowExceptionP009() throws Exception {
            //given - @BeforeEach
            int validProductId = productRepository.save(VALID_PRODUCT2.toEntity()).getId();
            //when
            MockHttpServletRequestBuilder requestBuilder = requestBuild(
                    validProductId, List.of() ,BASE_URL);

            //then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isBadRequest())
                    .andExpect(res -> assertBusinessException(getException(res)))
                    .andExpect(res -> assertResponseBodyCode(getErrorCode(res), "P009"))
                    .andDo(print());
        }
    }
}
