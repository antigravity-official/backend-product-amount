package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        Date now = new Date();

        Product product = repository.getProduct(request.getProductId());

        // 1. 쿠폰 적용 가능한 상품인지 체크
        if (repository.isUsableCouponsToProduct(request.getProductId(), request.getCouponIds())) {
            // 2. 사용 가능 기간인 쿠폰만 걸러내기
            List<Promotion> promotions = repository.getPromotions(request.getCouponIds()).stream()
                    .filter(promotion -> promotion.isUsable(now))
                    .toList();

            int finalPrice = getFinalPrice(product, promotions);

            return ProductAmountResponse.builder()
                    .name(product.getName())
                    .originPrice(product.getPrice())
                    .discountPrice(product.getPrice() - finalPrice)
                    .finalPrice(finalPrice)
                    .build();
        }

        return null;
    }

    public int getFinalPrice(Product product, List<Promotion> promotions) {
        int finalPrice = product.getPrice();

        List<Promotion> sortedPromotions = sortingPromotions(promotions);

        for (Promotion promotion : sortedPromotions) {
            if (Objects.equals(promotion.getDiscount_type(), "WON")) {
                finalPrice -= promotion.getDiscount_value();
            } else if (Objects.equals(promotion.getDiscount_type(), "PERCENT")) {
                finalPrice = finalPrice * (100 - promotion.getDiscount_value()) / 100;
            }
        }
        return finalPrice / 1000 * 1000;
    }

    public List<Promotion> sortingPromotions(List<Promotion> promotions) {
        return promotions.stream().sorted(
                (o1, o2) -> {
                    if (Objects.equals(o1.getDiscount_type(), "WON") && !Objects.equals(o2.getDiscount_type(), "WON")) {
                        // o1이 "WON"인 경우, o1이 o2보다 우선순위가 높음
                        return -1;
                    } else if (!Objects.equals(o1.getDiscount_type(), "WON") && Objects.equals(o2.getDiscount_type(), "WON")) {
                        // o2가 "WON"인 경우, o2가 o1보다 우선순위가 높음
                        return 1;
                    } else {
                        // o1과 o2의 type이 같은 경우, 기존의 순서를 유지함
                        return 0;
                    }
                }
        ).toList();
    }
}
