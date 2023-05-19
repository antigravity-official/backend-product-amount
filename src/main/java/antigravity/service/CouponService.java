package antigravity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Service;

import antigravity.domain.entity.Promotion;
import antigravity.error.ErrorCode;
import antigravity.exception.CouponException;
import antigravity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CouponService {
    private final ProductRepository repository;

    private int maxDiscountPrice;
    private boolean[] visited;

    // 쿠폰 적용 가능 여부 확인
    public void isCouponApplicable(int productId, int[] couponIds, ArrayList<HashMap<String, Integer>> couponList) throws CouponException {

        for (int couponId : couponIds) {
            
            Promotion promotionInfo = repository.findCouponName(couponId);


            // 1. 해당 상품에 적용 가능한 쿠폰인지 확인
            Optional<Promotion> optionalPromotion = repository.checkCouponAvailability(productId, couponId);

            if(!optionalPromotion.isPresent()){
                throw new CouponException(ErrorCode.INVALID_COUPON_FOR_PRODUCT, promotionInfo.getName());
            }

            Promotion promotion = optionalPromotion.get();


            // 2. 쿠폰 사용기간이 적합한지 확인
            Date currentDate = new Date();

            if (currentDate.before(promotion.getUse_started_at())) {
                throw new CouponException(ErrorCode.COUPON_NOT_YET_AVAILABLE, promotionInfo.getName());
            } else if (currentDate.after(promotion.getUse_ended_at())) {
                throw new CouponException(ErrorCode.COUPON_EXPIRED, promotionInfo.getName());
            }


            // 3. 사용 가능한 쿠폰 add
            HashMap<String, Integer> couponMap = new HashMap<>();
            couponMap.put(promotion.getDiscount_type(), promotion.getDiscount_value());
            couponList.add(couponMap);         
        }

    }


    // 최대 할인 금액 찾기
    public int findMaxDiscountPrice(int currentPrice, int depth, ArrayList<HashMap<String, Integer>> couponList) {

        if(couponList.size() == 0) {
            return currentPrice;
        }

        visited = new boolean[couponList.size()];
        maxDiscountPrice = currentPrice;

        fnMaxDiscountPrice(currentPrice, 0, couponList);
        
        return maxDiscountPrice;
    }


    // 선택된 쿠폰들로 최대 할인 금액 찾기 - 완전탐색 알고리즘
    private void fnMaxDiscountPrice(int currentPrice, int depth, ArrayList<HashMap<String, Integer>> couponList) {

        if(depth == couponList.size()) {
            maxDiscountPrice = Math.min(maxDiscountPrice, currentPrice / 1000 * 1000);
            return;
        }

        int discountedPrice = 0;
        for(int i = 0; i < couponList.size(); i++) {
            if(visited[i]) continue;
            
            String discountType = "";
            HashMap<String, Integer> coupon = couponList.get(i);
            if (!coupon.isEmpty()) {
                for (String key : coupon.keySet()) {
                    discountType = key;
                    break;
                }
            }
            int discount = coupon.get(discountType);

            if(discountType.equals("WON")) {
                discountedPrice = currentPrice - discount;
            }else if(discountType.equals("PERCENT")) {
                discountedPrice = (int) ((double) currentPrice * (1 - (double) discount/100));
            }

            visited[i] = true;
            fnMaxDiscountPrice(discountedPrice, depth + 1, couponList);
            visited[i] = false;
        }
    }
    
}
