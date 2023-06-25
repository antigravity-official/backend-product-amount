package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionProductsRepository promotionProductsRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {
        final String PROMOTION_COUPON = "COUPON";
        final String PROMOTION_CODE = "CODE";
        int discountPrice = 0; // 총 할인 금액
        int finalPrice = 0; // 확정 상품 가격

        Product product = productRepository.getProduct(request.getProductId());
        int productPrice = product.getPrice();

        // 프로모션_프로덕트 정보 조회
        List<PromotionProducts> promotionProductsList = promotionProductsRepository.getPromotionsProducts(product.getId());

        for (PromotionProducts promotionProducts : promotionProductsList) {
            //프로모션 정보 조회
            Promotion promotion = promotionRepository.getPromotion(promotionProducts.getPromotionId());
            String promotionType = promotion.getPromotion_type();
            int discountValue = promotion.getDiscount_value();

            if (PROMOTION_COUPON.equals(promotionType)) {
                // 할인쿠폰 프로모션 적용
                boolean useCouponFlag = checkUseCouponPossible(promotion);
                if (useCouponFlag) {
                    discountPrice += discountValue;
                }
            } else if (PROMOTION_CODE.equals(promotionType)) {
                // 할인코드 프로모션 적용
                discountPrice += (productPrice * discountValue) / 100;
            }
        }

        //가격 정책 적용
        finalPrice = validateFinalPrice(productPrice, discountPrice);

        //웅답데이터
        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(product.getPrice())
                .discountPrice(discountPrice)
                .finalPrice(finalPrice)
                .build();
    }

    /**
     * 쿠폰 적용 가능한지 날짜 체크
     * @param promotion 프로모션 정보
     * @return 쿠폰 적용 가능 여부 (true:가능 / false:불가능)
     */
    private boolean checkUseCouponPossible(Promotion promotion) {
        LocalDate currentTime = LocalDate.now();
        LocalDate useStartDate = convertDateToLocalDate(promotion.getUse_started_at());
        LocalDate useEndDate = convertDateToLocalDate(promotion.getUse_ended_at());

        if(currentTime.isAfter(useStartDate) && currentTime.isBefore(useEndDate)) {
            return true;
        } else {
            return false;
        }
    }

    private LocalDate convertDateToLocalDate(Date paramDate) {
        return LocalDate.ofInstant(paramDate.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 최소/최대 금엑 정책 적용, 천단위 절삭 계산
     * @param productPrice 상품 금액
     * @param discountPrice 할인 금액
     * @return 확정 상품 가격
     */
    private int validateFinalPrice(int productPrice, int discountPrice) {
        final int TEN_THOUSAND = 10000;
        final int A_MILLION = 1000000;
        int finalPrice = productPrice - discountPrice; // 프로모션 적용된 가격

        // 최소금액, 최대금액 정책 적용
        if(finalPrice < TEN_THOUSAND) {
            finalPrice = TEN_THOUSAND;
        } else if(finalPrice > A_MILLION) {
            finalPrice = A_MILLION;
        }

        // 천단위 절삭
        int downUnit = finalPrice % TEN_THOUSAND;

        return finalPrice - downUnit;
    }
}
