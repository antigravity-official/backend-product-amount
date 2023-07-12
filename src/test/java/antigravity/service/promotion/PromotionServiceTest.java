package antigravity.service.promotion;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import java.util.Collections;
import java.util.List;

import static antigravity.error.ErrorCode.NO_PROMOTIONS_AVAILABLE;
import static antigravity.error.ErrorCode.NO_REQUEST_PROMOTIONS;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * PromotionService
 * Service Layer - Mock Test
 */
@DisplayName("[Service] PromotionService - Service Layer Mock Test")
class PromotionServiceTest extends ServiceTestSupport {

    @Spy
    @InjectMocks
    private PromotionService promotionService;

    private static final int PRODUCT_ID = 1;
    private static final List<Integer> PROMOTION_IDS = of(1, 2);
    private static final List<Promotion> PROMOTIONS = of(VALID_PROMOTION1.toEntity(), VALID_PROMOTION2.toEntity());

    @Nested
    @DisplayName("[findMappingIdsByProductId] 상품 ID에 매핑된 유효한 프로덕트 아이디 리스트를 반환합니다.")
    class TestFindMappingIdsByProductId {
    }
}
