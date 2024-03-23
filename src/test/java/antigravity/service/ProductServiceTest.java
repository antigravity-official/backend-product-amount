package antigravity.service;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotion.enums.PromotionType;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

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
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(30_000)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        Promotion code = Promotion.builder()
                .promotionType(PromotionType.CODE)
                .name("15% 할인코드")
                .discountType(DiscountType.PERCENT)
                .discountValue(15)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.saveAll(List.of(coupon, code));

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        PromotionProducts productCode = PromotionProducts.builder()
                .product(product)
                .promotion(code)
                .build();
        promotionProductsRepository.saveAll(List.of(productCoupon, productCode));

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{coupon.getId(), code.getId()})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);

        ProductAmountResponse response = productService.getProductAmount(request, testDate);

        //then
        assertThat(response.getOriginPrice()).isEqualTo(215_000);
        assertThat(response.getDiscountPrice()).isEqualTo(62_250);
        assertThat(response.getFinalPrice()).isEqualTo(152_000);
    }

    @DisplayName("상품 id가 존재하지 않으면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest2() {
        //given
        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(9999)
                .couponIds(new int[]{9999, 9998})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);

        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("프로모션 id가 존재하지 않으면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest3() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{9999, 9998})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);

        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);

    }

    @DisplayName("프로모션 중 하나라도 상품 적용 대상이 아니면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest4() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(30_000)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.save(coupon);

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        promotionProductsRepository.save(productCoupon);

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{coupon.getId(), 9999})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);


        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);

    }

    @DisplayName("금액을 조회하는 시점이 프로모션의 유효기간과 다르면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest5() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(30_000)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.save(coupon);

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        promotionProductsRepository.save(productCoupon);

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{coupon.getId()})
                .build();
        LocalDate testDate = LocalDate.of(2023, 03,02);

        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);

    }


    @DisplayName("프로모션이 적용 된 확정 상품 금액이 10,000원 미만이면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest6() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(215_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("206000원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(206_000)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.save(coupon);

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        promotionProductsRepository.save(productCoupon);

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{coupon.getId()})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);

        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);

    }

    @DisplayName("프로모션이 적용 된 확정 상품 금액이 10,000,000원 초과이면 가격을 조회할 수 없다.")
    @Test
    void getProductAmountTest7() {
        //given
        Product product = Product.builder()
                .name("피팅노드상품")
                .price(10_002_000)
                .build();
        productRepository.save(product);

        Promotion coupon = Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("100원 할인쿠폰")
                .discountType(DiscountType.WON)
                .discountValue(100)
                .useStartedAt(LocalDate.of(2022, 11, 01))
                .useEndedAt(LocalDate.of(2023, 03, 01))
                .build();
        promotionRepository.save(coupon);

        PromotionProducts productCoupon = PromotionProducts.builder()
                .product(product)
                .promotion(coupon)
                .build();
        promotionProductsRepository.save(productCoupon);

        //when
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(product.getId())
                .couponIds(new int[]{coupon.getId()})
                .build();
        LocalDate testDate = LocalDate.of(2023, 01, 01);

        //then
        assertThatThrownBy(() -> productService.getProductAmount(request, testDate))
                .isInstanceOf(RuntimeException.class);

    }
}