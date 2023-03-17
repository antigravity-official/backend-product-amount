package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        System.out.println("상품 가격 추출 로직을 완성 시켜주세요.");

        Product product = repository.getProduct(request.getProductId());

        AtomicInteger discountPrice = new AtomicInteger();
        Arrays.stream(request.getCouponIds()).forEach(couponId -> {
            Promotion promotion = repository.getPromotion(couponId);

            int tmpDiscountPrice = 0;
            if ( "COUPON".equals(promotion.getPromotion_type()) )
                tmpDiscountPrice = promotion.getDiscount_value();
            else if ( "CODE".equals(promotion.getPromotion_type()) )
                // 100으로 나누면 0.15가 보존되지 않으므로 100.0으로 나눠서 처리함
                tmpDiscountPrice = (int)(product.getPrice() * ((long) promotion.getDiscount_value()/100.0));

            discountPrice.addAndGet(tmpDiscountPrice);
        });

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice.intValue())
                .finalPrice(product.getPrice() - discountPrice.intValue())
                .build();
    }
}
