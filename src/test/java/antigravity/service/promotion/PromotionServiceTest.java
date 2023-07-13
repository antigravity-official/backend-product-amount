package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.List;

import static antigravity.error.ErrorCode.*;
import static antigravity.global.fixture.PromotionFixture.*;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

// todo 테스트코드 작성 완료 7/12 15:14

/**
 * PromotionService
 * Service Layer - Mock Test
 */
@DisplayName("[Service] PromotionService - Service Layer Mock Test")
class PromotionServiceTest extends ServiceTestSupport {

    @InjectMocks
    private PromotionService promotionService;

    private static final int PRODUCT_ID = 1;
    private static final List<Integer> PROMOTION_IDS = of(1, 2);
    private static final List<Promotion> PROMOTIONS = of(VALID_FIX_PROMOTION1.toEntity(), VALID_RATE_PROMOTION2.toEntity());

    Promotion mockPromotion1 = mock(Promotion.class);
    Promotion mockPromotion2 = mock(Promotion.class);
    List<Promotion> mockPromotions = of(mockPromotion1, mockPromotion2);

    @Nested
    @DisplayName("[findMappingIdsByProductId] 상품 ID에 매핑된 유효한 프로덕트 아이디 리스트를 반환합니다.")
    class TestFindMappedIdsByProductId {

        @Test
        @DisplayName("[Success] 상품 Id에 매칭된 상품이 있어, 해당 프로모션 아이디 리스트를 반환한다.")
        void When_ExistencePromotionIdsRequested_Expect_ReturnPromotionIds() throws Exception {
            //given
            given(promotionProductsQueryRepo.findPromotionIdsByProductId(any()))
                    .willReturn(PROMOTION_IDS);

            //when
            List<Integer> foundPromotionIds = promotionService.findMappedPromotionIdsByProductId(PRODUCT_ID);

            //then
            assertNotNull(foundPromotionIds);
        }

        @Test
        @DisplayName("[Exception] 상품 Id에 매칭된 상품이 전혀 없어, 예외를 던진다")
        void When_NonExistencePromotionIdsRequested_Expect_ThrowException() throws Exception {
            //given
            given(promotionProductsQueryRepo.findPromotionIdsByProductId(PRODUCT_ID))
                    .willReturn(emptyList());

            //when && then
            assertThatThrownBy(() -> promotionService.findMappedPromotionIdsByProductId(PRODUCT_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(NO_PROMOTIONS_AVAILABLE.getMessage());
        }
    }

    @Nested
    @DisplayName("[findAllPromotionsByIds] 해당 프로모션 Ids에 해당하는 모든 프로모션을 반환한다.")
    class TestFindAllPromotionsByIds {

        @Test
        @DisplayName("[Success] 해당 프로모션 Ids에 해당하는 프로모션 리스트를 반환한다.")
        void When_ValidIdsRequested_Expect_ReturnPromotions() throws Exception {
            //given
            given(promotionQueryRepo.findPromotionsByIds(PROMOTION_IDS))
                    .willReturn(PROMOTIONS);
            //when
            List<Promotion> foundPromotions = promotionService.findAllPromotionsByIds(PROMOTION_IDS);

            //then
            assertNotNull(foundPromotions);
        }

        @Test
        @DisplayName("[Exception] 사용 기간이 도래하지 않은 쿠폰이 요청될 경우, 예외를 던진다.")
        void When_BeforeUsagePeriodPromotionIdsRequested_Expect_ThrowException() throws Exception {
            //given
            List<Integer> invalidPromotionIds = of(1);
            List<Promotion> invalidPromotions = of(BEFORE_USAGE_PERIOD_PROMOTION.toEntity());
            given(promotionQueryRepo.findPromotionsByIds(invalidPromotionIds))
                    .willReturn(invalidPromotions);

            //when && then
            assertThatThrownBy(() -> promotionService.findAllPromotionsByIds(invalidPromotionIds))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(INVALID_PROMOTION_PERIOD.getMessage());
        }

        @Test
        @DisplayName("[Exception] 사용 기간이 지나 만료된 쿠폰이 요청될 경우, 예외를 던진다.")
        void When_ExpiredPromotionIdsRequested_Expect_ThrowException() throws Exception {
            //given
            List<Integer> invalidPromotionIds = of(1);
            List<Promotion> invalidPromotions = of(EXPIRED_PROMOTION.toEntity());
            given(promotionQueryRepo.findPromotionsByIds(invalidPromotionIds))
                    .willReturn(invalidPromotions);

            //when && then
            assertThatThrownBy(() -> promotionService.findAllPromotionsByIds(invalidPromotionIds))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(INVALID_PROMOTION_PERIOD.getMessage());
        }
    }

    @Nested
    @DisplayName("[findApplicablePromotions] desiredPromotionIDs Ids가 availablePromotions에 모두 존재한다면, 매핑된 프로모션의 리스트를 반환한다.")
    class TestFindApplicablePromotions {

        @Test
        @DisplayName("[Success] 요청한 Ids가 availablePromotions에 모두 존재해, 프로모션 리스트를 반환한다.")
        void When_AvailablePromotionsContainEachDesiredPromotionIds_Expect_ReturnPromotions() throws Exception {
            //given
            given(mockPromotion1.getId()).willReturn(1);
            given(mockPromotion2.getId()).willReturn(2);

            //when
            List<Promotion> foundPromotions = promotionService.findApplicablePromotions(PROMOTION_IDS, mockPromotions);

            //then
            Assertions.assertNotNull(foundPromotions);
        }

        @Test
        @DisplayName("[Success] 요청한 Ids중 일부가 availablePromotions에  존재하지 않아, 예외를 던진다.")
        void When_SomePromotionsNotContainEachDesiredPromotionIds_Expect_ThrowException() throws Exception {
            //given
            given(mockPromotion1.getId()).willReturn(1);
            given(mockPromotion2.getId()).willReturn(3);

            //when && then
            assertThatThrownBy(() -> promotionService.findApplicablePromotions(PROMOTION_IDS, mockPromotions))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(NOT_AVAILABLE_PROMOTION.getMessage());
        }

        @Test
        @DisplayName("[Success] 요청한 Ids중 전부가 availablePromotions에 존재하지 않아, 예외를 던진다.")
        void When_AllPromotionsNotContainEachDesiredPromotionIds_Expect_ThrowException() throws Exception {
            //given
            given(mockPromotion1.getId()).willReturn(3);
            given(mockPromotion2.getId()).willReturn(4);

            //when && then
            assertThatThrownBy(() -> promotionService.findApplicablePromotions(PROMOTION_IDS, mockPromotions))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(NOT_AVAILABLE_PROMOTION.getMessage());
        }
    }

    @Nested
    @DisplayName("[verifyPromotionRequestExistence] 프로모션 아이디의 요청 여부를 검증하는 void 메소드")
    class TestVerifyPromotionRequestExistence {

        @Test
        @DisplayName("[Success] 프로모션 아이디가 정상적으로 요청되어, 성공한다.")
        void When_PromotionIdsRequested_Expect_Success() throws Exception {
            //given && when &&then
            assertDoesNotThrow(() -> promotionService.verifyPromotionRequestExistence(PROMOTION_IDS));
        }

        @Test
        @DisplayName("[Exception] 프로모션 아이디가 emptyList()가 요청되어, 예외를 던진다.")
        void When_PromotionIdsNotRequested_Expect_ThrowException() throws Exception {
            //given && when && then
            assertThatThrownBy(() -> promotionService.verifyPromotionRequestExistence(emptyList()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(NO_REQUEST_PROMOTIONS.getMessage());
        }
    }
}
