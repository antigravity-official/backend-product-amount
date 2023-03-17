package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.error.exception.BusinessException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static antigravity.error.dto.ErrorType.*;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionRepository promotionRepository;
    private final ValidatorService validatorService;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        // 상품 조회 불가 예외처리
        Product product = repository.getProduct(request.getProductId()).orElseThrow(() -> new BusinessException(
                NOT_FOUND_PRODUCT));

        // 상품 가격 예외처리
        validatorService.isValidPrice(product.getPrice());

        AtomicInteger discountPrice = new AtomicInteger();
        Arrays.stream(request.getCouponIds()).forEach(couponId -> {
            // 프로모션 조회 불가 예외처리
            Promotion promotion = promotionRepository.getPromotion(couponId).orElseThrow(() -> new BusinessException(
                    NOT_FOUND_PROMOTION));

            validatorService.validatePeriod(promotion.getUse_started_at(), promotion.getUse_ended_at());

            int tmpDiscountPrice = 0;
            if ( Promotion.DiscountType.COUPON.equals(promotion.getPromotion_type()) )
                tmpDiscountPrice = promotion.getDiscount_value();
            else if ( Promotion.DiscountType.CODE.equals(promotion.getPromotion_type()) )
                tmpDiscountPrice = (int)(product.getPrice() * ((long) promotion.getDiscount_value()/100.0));


            discountPrice.addAndGet(tmpDiscountPrice);
        });
        // 할인 적용 후 상품 금액 검증
        validatorService.validatePrice(product.getPrice(), discountPrice.intValue());

        int tmpFinalPrice = product.getPrice() - discountPrice.intValue();
        // 천원단위 절사 적용
        tmpFinalPrice = (tmpFinalPrice / 1000) * 1000;

        // 프로모션 적용 후 최종 상품가격 예외처리
        validatorService.isValidPrice(tmpFinalPrice);

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice.intValue())
                .finalPrice(tmpFinalPrice)
                .build();
    }


}
