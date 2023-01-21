package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @Transactional
    @DisplayName("서비스 로직 체크")
    void getProductAmount() throws Exception {
        System.out.println("상품 가격 추출 테스트");

        /*
            INSERT INTO product VALUES (1, '피팅노드상품', 215000);

            INSERT INTO promotion VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
            INSERT INTO promotion VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2023-03-01');
            INSERT INTO promotion VALUES (4, 'CODE', '20% 할인코드', 'PERCENT', 20, '2022-11-01', '2022-12-01');
            INSERT INTO promotion VALUES (5, 'CODE', '15% 할인코드', 'PERCENT', 15, '2023-02-01', '2023-03-01');
            INSERT INTO promotion VALUES (6, 'COUPON', '210000원 할인쿠폰', 'WON', 210000, '2022-11-01', '2023-03-01');
            INSERT INTO promotion VALUES (7, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
            INSERT INTO promotion VALUES (8, 'COUPON', '20% 할인쿠폰', 'PERCENT', 20, '2022-11-01', '2023-03-01');

            INSERT INTO promotion_products VALUES (1, 1, 1);
            INSERT INTO promotion_products VALUES (2, 2, 1);
            INSERT INTO promotion_products VALUES (3, 4, 1);
            INSERT INTO promotion_products VALUES (4, 5, 1);
            INSERT INTO promotion_products VALUES (5, 6, 1);
            INSERT INTO promotion_products VALUES (6, 8, 1);
         */
        int originPrice = 215000;
        int couponDiscount = 30000;
        double codeDiscount = originPrice * (15 / 100d);

        List<ProductInfoRequest> requests = new ArrayList<>();
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1, 2}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1, 1}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{2}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{3}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{1, 2}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{1, 2}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,4}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{2,5}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{6}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{1,6}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{7,6}).build());
        requests.add(ProductInfoRequest.builder()
                .productId(1)
                .couponIds(new int[]{8,1}).build());

        for (ProductInfoRequest request : requests) {
            Throwable thrown = catchThrowable(() -> productService.getProductAmount(request));

            if (request.getProductId() == 1) {
                List<Integer> overlap = new ArrayList<>();
                boolean isOverlap = false;
                for (int couponId : request.getCouponIds()) {
                    if (overlap.contains(couponId)) {
                        isOverlap = true;
                    } else {
                        overlap.add(couponId);
                    }
                }
                if (isOverlap) {
                    assertThat(thrown).isInstanceOf(Exception.class)
                            .hasMessageContaining("중복된 프로모션은 적용 불가합니다.");
                } else if (Arrays.stream(request.getCouponIds())
                        .anyMatch(d -> d == 3 || d == 4 || d == 5 || d == 7 || d == 8)) {
                    assertThat(thrown).isInstanceOf(Exception.class)
                            .hasMessageContaining("적용 프로모션 중 유효하지 않은 프로모션이 있습니다.");
                } else if (Arrays.stream(request.getCouponIds()).anyMatch(d -> d == 6 )) {
                    assertThat(thrown).isInstanceOf(Exception.class)
                            .hasMessageContaining("최종 가격이 ₩ 10,000 을 미달하거나, ₩ 10,000,000을 초과합니다.");
                }else {
                    ProductAmountResponse response = productService.getProductAmount(request);
                    int testDiscountPrice = originPrice, testFinalPrice = 0;
                    double testDiscountAmount = 0d;

                    for (int couponId : request.getCouponIds()) {
                        if (couponId == 1) {
                            testDiscountAmount += couponDiscount;
                        } else if (couponId == 2) {
                            testDiscountAmount += codeDiscount;
                        }
                    }
                    testDiscountPrice -= testDiscountAmount;
                    testFinalPrice = testDiscountPrice - (testDiscountPrice % 1000);

                    assertThat(response)
                            .returns("피팅노드상품", from(ProductAmountResponse::getName))
                            .returns(originPrice, from(ProductAmountResponse::getOriginPrice))
                            .returns(testDiscountPrice, from(ProductAmountResponse::getDiscountPrice))
                            .returns(testFinalPrice, from(ProductAmountResponse::getFinalPrice));
                }
            } else {
                assertThat(thrown).isInstanceOf(Exception.class)
                        .hasMessageContaining("가격 추출 할 상품이 존재하지 않습니다.");
            }
        }
    }
}