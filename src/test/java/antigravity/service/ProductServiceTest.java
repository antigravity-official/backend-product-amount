package antigravity.service;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotion.enums.PromotionType;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import antigravity.exception.BizException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static antigravity.domain.entity.promotion.enums.DiscountType.PERCENT;
import static antigravity.domain.entity.promotion.enums.DiscountType.WON;
import static antigravity.domain.entity.promotion.enums.PromotionType.CODE;
import static antigravity.domain.entity.promotion.enums.PromotionType.COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PromotionProductsRepository promotionProductsRepository;

    @AfterEach
    void tearDown() {
        promotionProductsRepository.deleteAllInBatch();
        promotionRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("상품 id와 프로모션 id로 (기존 가격-총 할인 금액)의 1,000원 단위를 절삭한 확정 상품 가격을 조회한다.")
    @Test
    void getProductAmountTest1() {
        //given
        Product product = createProductWithPrice(215_000);
        Promotion coupon = createPromotionAs(COUPON, WON, 30_000);
        Promotion code = createPromotionAs(CODE, PERCENT, 15);

        productRepository.save(product);
        promotionRepository.saveAll(List.of(coupon, code));
        promotionProductsRepository.saveAll(
                List.of(createPromotionProducts(product, coupon), createPromotionProducts(product, code))
        );

        ProductInfoRequest request = createRequest(product.getId(), new int[]{coupon.getId(), code.getId()});
        LocalDate availableDate = getAvailableDate(coupon);

        //when
        ProductAmountResponse response = productService.getProductAmount(request, availableDate);

        //then
        assertThat(response.getOriginPrice()).isEqualTo(215_000);
        assertThat(response.getDiscountPrice()).isEqualTo(62_250);
        assertThat(response.getFinalPrice()).isEqualTo(152_000);
        assertThat(response.isPurchasableRightAway()).isTrue();
    }

    @DisplayName("상품 id가 존재하지 않으면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest2() {
        //given
        ProductInfoRequest request = createRequest(9999, new int[]{9999, 9998});
        LocalDate testDate = LocalDate.of(2024, 3, 23);

        //when  //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("product not found");
    }

    @DisplayName("프로모션 id가 존재하지 않으면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest3() {
        //given
        Product product = createProductWithPrice(215_000);
        productRepository.save(product);

        ProductInfoRequest request = createRequest(product.getId(), new int[]{9999, 9998});
        LocalDate testDate = LocalDate.of(2023, 1, 1);

        //when  //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("some promotions not found");
    }

    @DisplayName("프로모션 중 하나라도 상품 적용 대상이 아니면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest4() {
        //given
        Product product = createProductWithPrice(215_000);
        Promotion coupon1 = createPromotionAs(COUPON, WON, 30_000);
        Promotion coupon2 = createPromotionAs(COUPON, WON, 10_000);

        productRepository.save(product);
        promotionRepository.saveAll(List.of(coupon1,coupon2));
        promotionProductsRepository.save(createPromotionProducts(product, coupon1));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{coupon2.getId()});
        LocalDate availableDate = getAvailableDate(coupon1);

        //when  //then
        assertThatThrownBy(() -> productService.getProductAmount(request, availableDate))
                .isInstanceOf(BizException.class)
                .hasMessage("some promotions are not allowed for this product");
    }

    @DisplayName("프로모션 id가 안들어오면 할인 없이 기본 금액이 확정 금액이 된다.")
    @Test
    void getProductAmountTest5() {
        //given
        Product product = createProductWithPrice(215_000);
        Promotion coupon1 = createPromotionAs(COUPON, WON, 30_000);
        Promotion coupon2 = createPromotionAs(COUPON, WON, 10_000);

        productRepository.save(product);
        promotionRepository.saveAll(List.of(coupon1,coupon2));
        promotionProductsRepository.save(createPromotionProducts(product, coupon1));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{});
        LocalDate availableDate = getAvailableDate(coupon1);

        //when
        ProductAmountResponse response = productService.getProductAmount(request, availableDate);

        //then
        assertThat(response.getOriginPrice()).isEqualTo(215_000);
        assertThat(response.getDiscountPrice()).isEqualTo(0);
        assertThat(response.getFinalPrice()).isEqualTo(215_000);
        assertThat(response.isPurchasableRightAway()).isTrue();
    }

    @DisplayName("금액을 조회하는 시점이 프로모션의 유효기간과 다르면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest6() {
        //given
        Product product = createProductWithPrice(215_000);
        Promotion coupon = createPromotionAs(COUPON, WON, 30_000);

        productRepository.save(product);
        promotionRepository.save(coupon);
        promotionProductsRepository.save(createPromotionProducts(product, coupon));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{coupon.getId()});
        LocalDate notAvailableDate = coupon.getUseEndedAt().plusDays(1);

        //when  //then
        assertThatThrownBy(() -> productService.getProductAmount(request, notAvailableDate))
                .isInstanceOf(BizException.class)
                .hasMessageContaining(" is not available on ");
    }

    @DisplayName("프로모션이 적용 된 확정 상품 금액은 10,000원 이상이다.")
    @Test
    void getProductAmountTest7() {
        //given
        Product product = createProductWithPrice(101_000);
        Promotion promotion = createPromotionAs(COUPON, WON, 100);

        productRepository.save(product);
        promotionRepository.save(promotion);
        promotionProductsRepository.save(createPromotionProducts(product, promotion));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{promotion.getId()});
        LocalDate availableDate = getAvailableDate(promotion);

        //when
        ProductAmountResponse response = productService.getProductAmount(request, availableDate);

        //then
        assertThat(response.getOriginPrice()).isEqualTo(101_000);
        assertThat(response.getDiscountPrice()).isEqualTo(100);
        assertThat(response.getFinalPrice()).isEqualTo(100_000);
        assertThat(response.isPurchasableRightAway()).isTrue();
    }

    @DisplayName("프로모션이 적용 된 확정 상품 금액은 10,000,000원 이하이다.")
    @Test
    void getProductAmountTest8() {
        //given
        Product product = createProductWithPrice(10_001_000);
        Promotion promotion = createPromotionAs(COUPON, WON, 100);

        productRepository.save(product);
        promotionRepository.save(promotion);
        promotionProductsRepository.save(createPromotionProducts(product, promotion));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{promotion.getId()});
        LocalDate availableDate = getAvailableDate(promotion);

        //when
        ProductAmountResponse response = productService.getProductAmount(request, availableDate);

        //then
        assertThat(response.getOriginPrice()).isEqualTo(10_001_000);
        assertThat(response.getDiscountPrice()).isEqualTo(100);
        assertThat(response.getFinalPrice()).isEqualTo(10_000_000);
        assertThat(response.isPurchasableRightAway()).isTrue();

    }

    private static Stream<Arguments> provideProductsToTestFinalPrice() {
        return Stream.of(
                Arguments.of(createProductWithPrice(10_000)),
                Arguments.of(createProductWithPrice(10_002_000))
        );
    }
    @DisplayName("프로모션이 적용 된 확정 상품 금액이 10,000원 미만이거나 10,000,000원 초과이면 바로구매를 할 수 없다.")
    @MethodSource("provideProductsToTestFinalPrice")
    @ParameterizedTest
    void getProductAmountTest9(Product product) {
        //given
        Promotion promotion = createPromotionAs(COUPON, WON, 100);

        productRepository.save(product);
        promotionRepository.save(promotion);
        promotionProductsRepository.save(createPromotionProducts(product, promotion));

        ProductInfoRequest request = createRequest(product.getId(), new int[]{promotion.getId()});
        LocalDate availableDate = getAvailableDate(promotion);

        //when
        ProductAmountResponse response = productService.getProductAmount(request, availableDate);

        //then
        assertThat(response.getDiscountPrice()).isEqualTo(100);
        assertThat(response.isPurchasableRightAway()).isFalse();
    }



    private static Product createProductWithPrice(int price) {
        return Product.builder()
                .name("피팅노드상품")
                .price(price)
                .build();
    }

    private static Promotion createPromotionAs(PromotionType promotionType, DiscountType discountType, int discountValue) {
        return Promotion.builder()
                .name("sample promotion")
                .promotionType(promotionType)
                .discountType(discountType)
                .discountValue(discountValue)
                .useStartedAt(LocalDate.of(2022, 11, 1))
                .useEndedAt(LocalDate.of(2023, 3, 1))
                .build();
    }

    private static PromotionProducts createPromotionProducts(Product product, Promotion coupon) {
        return PromotionProducts.builder()
                .promotion(coupon)
                .product(product)
                .build();
    }

    private static ProductInfoRequest createRequest(int productId, int[] promotionIds) {
        return ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(promotionIds)
                .build();
    }

    private static LocalDate getAvailableDate(Promotion promotion) {
        return promotion.getUseStartedAt();
    }

}