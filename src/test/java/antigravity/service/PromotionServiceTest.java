package antigravity.service;

import antigravity.domain.entity.*;
import antigravity.repository.PromotionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {

    @InjectMocks
    private PromotionService service;
    @Mock
    private PromotionRepository repository;
    @Mock
    private DiscountTypeFactory discountTypeFactory;

    @Test
    @DisplayName("정상 작동")
    void getDiscountPrice() {
        // given
        Product product = getProduct();
        Promotion promotion = getCoupon1();
        List<Promotion> promotionList = Arrays.asList(promotion);
        List<Integer> promotionIds = promotionList.stream().map(Promotion::getId).collect(Collectors.toList());

        // stub
        given(repository.getPromotionBy(product.getId(), promotionIds)).willReturn(promotionList);
        given(discountTypeFactory.get(promotion.getDiscountType())).willReturn(new WonDiscountTypeService());

        // when
        Money discountPrice = service.getDiscountPrice(product, promotionIds);

        // then
        assertThat(discountPrice).isEqualTo(promotion.getDiscountValue());
    }

    @Test
    @DisplayName("유효한 쿠폰이 없는 경우")
    void getProductAmountWithoutValidCoupon() {
        // given
        Product product = getProduct();
        List<Promotion> promotionList = getPromotions();
        List<Integer> promotionIds = promotionList.stream().map(Promotion::getId).collect(Collectors.toList());

        // stub
        given(repository.getPromotionBy(product.getId(), promotionIds)).willReturn(new ArrayList<>());

        // when
        Money discountPrice = service.getDiscountPrice(product, promotionIds);

        // then
        assertThat(discountPrice).isEqualTo(new Money(BigDecimal.ZERO));
    }

    Product getProduct() {
        return Product.builder()
                .id(1)
                .name("product1")
                .price(new Money(new BigDecimal(100000)))
                .build();
    }

    List<Promotion> getPromotions() {
        return Arrays.asList(getCoupon1(), getCode1());
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
