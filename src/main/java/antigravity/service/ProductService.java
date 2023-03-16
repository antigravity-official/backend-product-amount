package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final ValidateService validateService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        AtomicInteger discountPrice = new AtomicInteger();

        Product product = repository.getProduct(request.getProductId());

        Arrays.stream(request.getCouponIds()).forEach(couponId -> {
            // 1. Promotion 조회
            Promotion promotion = repository.getPromotion(couponId);

            // 2. 사용 가능 기간 확인
            validateService.validatePeriod(promotion.getUse_started_at(), promotion.getUse_ended_at());

            // 3. 할인 금액 계산
            int tempDiscountPrice = promotion.getDiscount_type() == WON ?
                    promotion.getDiscount_value() : (int) (product.getPrice() * ((long) promotion.getDiscount_value()/100.0));

            // 4. 할인 금액이 상품 금액을 초과 하는 지 확인
            validateService.validatePrice(product.getPrice(), tempDiscountPrice);

            // 5. 할인 금액 합산
            discountPrice.addAndGet(tempDiscountPrice);
        });

        // 6. 최종 할인 금액이 상품 금액을 초과 하는 지 확인
        validateService.validatePrice(product.getPrice(), discountPrice.intValue());

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice.intValue())
                .finalPrice(product.getPrice() - discountPrice.intValue())
                .build();

    }
}
