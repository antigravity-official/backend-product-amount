package antigravity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.fixture.ProductFixture;
import antigravity.fixture.PromotionFixture;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("- '가격산정' 기능 성공 테스트 케이스")
public class CalculatePriceServiceTest {

    @Autowired
    CalculatePriceService calculatePriceService;
    @MockBean private ProductRepository productRepository;
    @MockBean private PromotionProductsRepository promotionProductsRepository;

    private Product product1;
    private Promotion promotion1;
    private Promotion promotion2;
    private Promotion promotion3;
    private Promotion promotion4;

    @BeforeEach
    void setUp() {
        product1 = ProductFixture.getProduct(); // 215,000원 상품
        promotion1 = PromotionFixture.getPromotion(); // CODE : 15% 할인코드
        promotion2 = PromotionFixture.getPromotion2();  // COUPON : 30,000원 할인쿠폰
        promotion3 = PromotionFixture.getPromotion3(); // CODE : 10% 할인코드
        promotion4 = PromotionFixture.getPromotion4(); // COUPON : 50,888원 할인쿠폰
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 1] [정률 + 정액] " +
        "215,000원 상품에 30,000원 정액 프로모션과 15% 정률 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 157,000원이고 할인 금액은 58,000원이다.")
    void test1() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{1, 2})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion1)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion2)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        // then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(58000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(157000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 2] [정률 + 정률] " +
        "215,000원 상품에 10% 정률 프로모션과 15% 정률 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 164,000원이고 할인 금액은 51,000원이다.")
    void test2() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{1, 3})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion1)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion3)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        // then
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(51000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(164000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 3] [정액 + 정액] " +
        "215,000원 상품에 30,000원 정액 프로모션과 50,888원 정액 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 134,000원이고 할인 금액은 81,000원이다.")
    void test3() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{2, 4})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion2)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion4)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(81000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(134000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 4] [정률] " +
        "215,000원 상품에 10% 정률 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 193,000원이고 할인 금액은 22,000원이다.")
    void test4() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{3})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion3)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(22000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(193000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 5] [정액] " +
        "215,000원 상품에 50,888원 정액 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 164,000원이고 할인 금액은 51,000원이다.")
    void test5() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{4})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion4)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(51000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(164000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 5] [정액 + 정률 + 정액] " +
        "215,000원 상품에 30,000원 정액 프로모션과 10% 정률 프로모션과 50,888원 정액 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 112,000원이고 할인 금액은 103,000원이다.")
    void test6() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{2, 3, 4})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion2)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion3)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion4)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(95000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(120000)
        );
    }

    @Test
    @DisplayName("[가격산정 성공 CASE 5] [정률 + 정액 + 정률] " +
        "215,000원 상품에 15% 정률 프로모션과 30,000원 정액 프로모션과 10% 정률 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 112,000원이고 할인 금액은 103,000원이다.")
    void test7() {
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[]{1, 2, 3})
            .build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(product1));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            List.of(
                PromotionProducts
                    .builder()
                    .promotion(promotion1)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion2)
                    .product(product1)
                    .build(),
                PromotionProducts
                    .builder()
                    .promotion(promotion3)
                    .product(product1)
                    .build()
            ));

        ProductAmountResponse response = calculatePriceService.getProductAmount(productInfoRequest);
        assertAll(
            () -> assertThat(response.getName()).isEqualTo(product1.getName()),
            () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
            () -> assertThat(response.getDiscountPrice()).isEqualTo(74000),
            () -> assertThat(response.getFinalPrice()).isEqualTo(141000)
        );
    }
}
