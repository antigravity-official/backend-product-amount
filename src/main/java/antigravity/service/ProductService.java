package antigravity.service;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;
import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotionproducts.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;


    public ProductAmountResponse getProductAmount(ProductInfoRequest request, LocalDate now) {
        //상품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("product not found"));
        //프로모션 조회
        List<Integer> promotionIds = Arrays.stream(request.getCouponIds())
                .boxed()
                .collect(Collectors.toList());
        List<Promotion> promotions = promotionRepository.findAllById(promotionIds);

        if(promotions.size() != request.getCouponIds().length) throw new RuntimeException("some promotions not found");

        //프로모션 상품 적용 여부 검증
        List<PromotionProducts> promotionProducts = product.getPromotionProducts();
        List<Promotion> promotionsFromProduct = promotionProducts.stream()
                .map(PromotionProducts::getPromotion)
                .collect(Collectors.toList());
        if(!promotionsFromProduct.containsAll(promotions)) throw new RuntimeException("some promotions are not allowed for this product");

        //프로모션 유효기간 검증
        promotionsFromProduct.forEach(promotion -> {
                    if(now.isBefore(promotion.getUseStartedAt()) ||
                            now.isAfter(promotion.getUseEndedAt())) {
                        throw new RuntimeException("some promotions are not available today");
                    }
                });

        //프로모션 총 할인 금액 계산
        int discountPrice = 0;
        for(Promotion promotion : promotionsFromProduct) {
            DiscountType discountType = promotion.getDiscountType();
            if(discountType.equals(DiscountType.WON)) {
                discountPrice += promotion.getDiscountValue();
            } else if(discountType.equals(DiscountType.PERCENT)) {
                discountPrice += product.getPrice() * promotion.getDiscountValue() / 100;
            }
        }

        //확정 상품 금액 검증
        int finalPrice = (int) Math.floor((product.getPrice() - discountPrice) / 1000) * 1000;
        if(finalPrice < 10_000 || finalPrice > 10_000_000) throw new RuntimeException("final price is not allowed for order");

        return ProductAmountResponse.of(product, discountPrice, finalPrice);
    }
}
