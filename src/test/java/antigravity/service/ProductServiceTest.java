package antigravity.service;

import antigravity.exception.BaseApiException;
import antigravity.exception.ProductNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.rds.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active:dev", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceTest {

    @Autowired
    private ProductService productService;


    @Test
    @DisplayName("정상 case")
    void getProductAmount() {

        int[] couponIds = {3, 4};

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            assertTrue(false);
            return;
        }

        assertTrue(true);
    }

    @Test
    @DisplayName("상품존재하지 않는 Err case : errCode=1000")
    void getProductAmountErr1000() {

        int[] couponIds = {1, 2};

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(100) // 없는 상품
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1000)) {
                assertTrue(true);
                return;
            }
        }

        assertTrue(false);
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는 Err case : errCode=1002")
    void getProductAmountErr1002() {

        int[] couponIds = {100}; // 없는 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1002)) {
                assertTrue(true);
                return;
            }
        }

        assertTrue(false);
    }

    @Test
    @DisplayName("쿠폰 유효기간 시작전 Err case : errCode=1005")
    void getProductAmountErr1005() {

        int[] couponIds = {1}; // 요효기간 이전인 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1005)) {
                assertTrue(true);
                return;
            }
        }

        assertTrue(false);
    }

    @Test
    @DisplayName("쿠폰 유효기간 지난 Err case : errCode=1006")
    void getProductAmountErr1006() {

        int[] couponIds = {2}; // 요효기간 지난 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1006)) {
                assertTrue(true);
                return;
            }

        }

        assertTrue(false);
    }

    @Test
    @DisplayName("상품과 쿠폰이 매핑되지 않은 Err case : errCode=1001")
    void getProductAmountErr1001() {

        int[] couponIds = {5}; // 쿠폰은 존재하나 상품과 매핑이 안되어 있는 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1001)) {
                assertTrue(true);
                return;
            }

        }

        assertTrue(false);
    }


    @Test
    @DisplayName("같은 PromotionType의 쿠폰을 한개이상 적용했을 시 Err case : errCode=1003")
    void getProductAmountErr1003() {

        int[] couponIds = {4, 6}; // 4,6번 쿠폰 모두 % 할인 타입의 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(1)
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1003)) {
                assertTrue(true);
                return;
            }

        }

        assertTrue(false);
    }

    @Test
    @DisplayName("최종금액이 10000원보다 작을 시 Err case : errCode=1004")
    void getProductAmountErr1004() {

        int[] couponIds = {7}; // 2번 상품과 매핑되어 있는 할인 쿠폰

        try {

            ProductInfoRequest request = ProductInfoRequest.builder()
                    .productId(2) // 10000원짜리 상품
                    .couponIds(couponIds)
                    .build();
            ProductAmountResponse productAmountResponse = productService.getProductAmount(request);

        } catch (BaseApiException e) {
            if(e.getCode().equals(1004)) {
                assertTrue(true);
                return;
            }

        }

        assertTrue(false);
    }








}