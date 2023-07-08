package antigravity.service;

import antigravity.domain.entity.*;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    @Mock
    private PromotionService promotionService;

    @Test
    @DisplayName("정상 동작")
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");

        // given
        Product product = getProduct();
        Promotion promotion = getCode1();
        ProductInfoRequest req = ProductInfoRequest
                .builder()
                .productId(product.getId())
                .couponIds(Arrays.asList(promotion.getId()))
                .build();
        Money discountPrice = product.getPrice()
                .multiply(promotion.getDiscountValue())
                .divide(new Money(new BigDecimal(100)));

        // stub
        given(repository.getProduct(product.getId())).willReturn(product);
        given(promotionService.getDiscountPrice(product, Arrays.asList(promotion.getId()))).willReturn(discountPrice);

        // when
        ProductAmountResponse productAmount = service.getProductAmount(req);

        // then
        BigDecimal finalPrice = product.getPrice().minus(discountPrice).getAmount();
        assertThat(productAmount.getFinalPrice()).isEqualTo(finalPrice);
    }

    @Test
    @DisplayName("할인가격이 상품 가격보다 큰 경우")
    void getProductAmountWithDiscountPriceIsGreaterThanOriginPrice() {
        // given
        Product product = getProduct();
        ProductInfoRequest req = ProductInfoRequest
                .builder()
                .productId(product.getId())
                .couponIds(Arrays.asList(1))
                .build();
        Promotion promotion = getCoupon1();
        List<Promotion> promotionList = Arrays.asList(promotion);
        List<Integer> promotionIds = promotionList.stream().map(Promotion::getId).collect(Collectors.toList());

        // stub
        given(repository.getProduct(product.getId())).willReturn(product);

        Money discountPrice = product.getPrice().plus(new Money(new BigDecimal(10000)));
        given(promotionService.getDiscountPrice(product, promotionIds)).willReturn(discountPrice);

        // when
        ProductAmountResponse productAmount = service.getProductAmount(req);

        // then
        assertThat(productAmount.getFinalPrice()).isEqualTo(BigDecimal.ZERO);
    }

    Product getProduct() {
        return Product.builder()
                .id(1)
                .name("product1")
                .price(new Money(new BigDecimal(100000)))
                .build();
    }

    Promotion getCoupon1() {
        return Promotion.builder()
                .id(1)
                .promotionType(PromotionType.COUPON)
                .name("coupon1")
                .discountType(DiscountType.WON)
                .discountValue(new Money(new BigDecimal(60000)))
                .build();
    }

    Promotion getCode1() {
        return Promotion.builder()
                .id(2)
                .promotionType(PromotionType.CODE)
                .name("code1")
                .discountType(DiscountType.PERCENT)
                .discountValue(new Money(new BigDecimal(10)))
                .build();
    }
}