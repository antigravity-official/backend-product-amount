package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductPriceService {

    private final DiscountService discountService;

    public ProductAmountResponse determineToApplyDiscount(Product product, List<Promotion> promotions, boolean request) {
        if (request) {
            return applyDiscountPromotions(product, promotions);
        } else {
            return notApplyDiscountPromotions(product);
        }
    }

    /**
     * @param product    - 할인 적용을 원하는 상품
     * @param promotions - 유효성 검증이 끝나, 모두 적용이 가능한 프로모션
     * @return - ProductAmountResponse 객체로 리턴
     */
    public ProductAmountResponse applyDiscountPromotions(Product product, List<Promotion> promotions) {
        final int originPrice = product.getPrice();
        final int finalDiscountedAmount = discountService.getFinalDiscountedAmount(originPrice, promotions);
        final int finalDiscountedPrice = discountService.getFinalDiscountedPrice(originPrice, finalDiscountedAmount);
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountAmount(finalDiscountedAmount)
                .finalPrice(finalDiscountedPrice)
                .build();
    }

    /**
     * @param product    - 상품
     * @return - ProductAmountResponse 객체로 할인하지 않은 객체로 리턴
     */
    public ProductAmountResponse notApplyDiscountPromotions(Product product) {
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountAmount(0)
                .finalPrice(product.getPrice())
                .build();
    }


}
