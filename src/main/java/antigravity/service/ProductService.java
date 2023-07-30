package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) {

        int totalDiscountValue = 0;

        int productId = request.getProductId();
        Product product = productRepository.getProduct(productId);

        int productPrice = product.getPrice();

        for (int promotionId : request.getCouponIds()
        ) {

            // 1. 쿠폰 조회
            Promotion promotion = getPromotion(productId, promotionId);
            if (promotion == null) continue;

            // 2. 쿠폰 사용가능 기간 체크
            boolean isValidDate = chkPromotionUseDate(promotionId, promotion.getUse_started_at(), promotion.getUse_ended_at());
            if (!isValidDate) continue;

            // 3. 쿠폰 타입에 따른 할인 금액 조회
            int currentPrice = productPrice - totalDiscountValue;
            int discountValue = getDiscountValue(promotion.getPromotion_type(), currentPrice, promotion.getDiscount_value());

            // 4. 할인 금액 추가
            totalDiscountValue += discountValue;

            // 5. 할인금액이 원가 이상일 경우 확정 상품 가격을 0으로 반환
            if (totalDiscountValue >= productPrice) {
                return ProductAmountResponse.builder()
                        .name(product.getName())
                        .originPrice(productPrice)
                        .discountPrice(productPrice)
                        .finalPrice(0)
                        .build();
            }
        }

        // 절삭 후 dto 반환
        int finalPrice = (int) (Math.floor((productPrice - totalDiscountValue) / 1000) * 1000);

        ProductAmountResponse response = ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(productPrice)
                .discountPrice(productPrice - finalPrice)
                .finalPrice(finalPrice)
                .build();

        return response;
    }

    // 프로모션 조회, 존재하지 않을 시 Null 반환
    private Promotion getPromotion(int productId, int promotionId) {
        try {
            return promotionRepository.getPromotion(productId, promotionId);
        } catch (EmptyResultDataAccessException e) {
            log.error("존재하지 않는 프로모션입니다. productId = " + productId + " promotionId = " + promotionId);
        }

        return null;
    }

    // 현재 시간을 기준으로 쿠폰 사용가능 기간 체크
    private boolean chkPromotionUseDate(int promotionId, Date startedAt, Date endedAt) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String promotionIdAndToday = "promotionId = " + promotionId + " today = " + simpleDateFormat.format(date);

        if (date.before(startedAt)) {
            log.error("아직 사용불가능한 쿠폰입니다. " + promotionIdAndToday + " startedAt = " + startedAt);
            return false;
        }

        if (date.after(endedAt)) {
            log.error("사용기간이 만료된 쿠폰입니다. " + promotionIdAndToday + " endedAt = " + endedAt);
            return false;
        }

        return true;
    }

    // 할인 금액 조회
    private int getDiscountValue(String promotionType, int currentPrice, int discountValue) {

        if (promotionType.equals("COUPON")) return discountValue;

        if (promotionType.equals("CODE")) {
            return currentPrice * discountValue / 100;
        }

        if (!promotionType.equals("COUPON") && !promotionType.equals("CODE")) {
            log.error("유효하지 않은 쿠폰타입 입니다. promotionType = " + promotionType);
        }

        return 0;
    }
}
