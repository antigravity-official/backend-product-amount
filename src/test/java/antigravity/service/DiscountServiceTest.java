package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.global.ProductFixture;
import antigravity.global.base.ServiceTestSupport;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static antigravity.global.ProductFixture.VALID_PRODUCT1;


class DiscountServiceTest extends ServiceTestSupport {

    @Test
    void 상품_가격_정상_산출() {
        Product product = VALID_PRODUCT1.toEntity();
        Promotion promotion1 = new Promotion(1, "COUPON", "30000원 할인쿠폰", "WON", 30000, LocalDate.parse("2022-11-01"), LocalDate.parse("2023-06-05");
    }
}
