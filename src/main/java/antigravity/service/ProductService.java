package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;
    private final PromotionRepository promotionRepository;

    public ProductAmountResponse getProductAmount(ProductInfoRequest request) throws ParseException {

        Product product = repository.getProduct(request.getProductId());
        int originPrice = product.getPrice();
        double discountSum = 0;
        // 쿠폰번호 메세지용
        int countNum = 0;

        for (int couponId : request.getCouponIds()) {
            // couponId에 해당하는 promotion 객체
            Promotion promotion = promotionRepository.getPromotion(couponId);
            countNum++;

            // 쿠폰이 유효하지 않을때
            if (!isValidatePromotion(promotion)) {
                System.out.println(countNum + "번째 쿠폰이 유효하지 않습니다.");
                continue;
            }

            discountSum += discountPrice(originPrice, promotion);
        }

        int finalPrice = finalCut(originPrice - discountSum);
        finalPrice = confirmRange(finalPrice);

        return ProductAmountResponse.builder()
                .name(product.getName())
                .originPrice(originPrice)
                .discountPrice(originPrice - finalPrice)
                .finalPrice(finalPrice)
                .build();
    }

    // 가격 범위가 1만 ~ 천만
    private int confirmRange(int finalPrice) {
        return Math.max(10000, Math.min(finalPrice, 10000000));
    }

    // 백단위 금액 자르기
    private int finalCut(double finalPrice) {
        return (int)finalPrice / 1000 * 1000;
    }

    // 할인 가격 계산
    private double discountPrice(int originPrice, Promotion promotion) {
        // 퍼센트 타입
        if(promotion.getDiscount_type().equals("PERCENT")) {
            return (double)originPrice * promotion.getDiscount_value() / 100;
        }
        // WON 타입
        return promotion.getDiscount_value();
    }

    // 쿠폰 유효 검사
    private boolean isValidatePromotion(Promotion promotion) throws ParseException {
        // 현재 날짜를 원하는 날짜 형식으로 바꿉니다.
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString());
        
        return date.compareTo(promotion.getUse_started_at()) >= 0 && date.compareTo(promotion.getUse_ended_at()) <= 0;
    }
}
