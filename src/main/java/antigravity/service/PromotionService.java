package antigravity.service;

import static antigravity.error.dto.ErrorType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import antigravity.domain.entity.Promotion;
import antigravity.error.exception.BusinessException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionService {

    private final PromotionRepository repository;

    public List<Promotion> getPromotionList(ProductInfoRequest request) {
        List<Promotion> promotionList = new ArrayList<>();

        if (request.getCouponIds() == null) {
            return promotionList;
        }

        if (isDuplicated(request.getCouponIds())) { // 중복 체크
            throw new BusinessException(DUPLICATED_PROMOTION);
        }

        for (int couponId : request.getCouponIds()) {

            Promotion promotion = repository.getPromotionById(request.getProductId(), couponId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_PROMOTION));

            if (!isValidPeriod(promotion)) { // 유효기간 검사
                throw new BusinessException(INVALID_PROMOTION_PERIOD);
            }

            promotionList.add(promotion);
        }

        return promotionList;
    }

    public int getDiscountPrice(int nowPrice, Promotion promotion) {

        Promotion.DiscountType discountType = Promotion.DiscountType.findEnumByCode(promotion.getPromotion_type());
        int discountValue = promotion.getDiscount_value();

        if (Promotion.DiscountType.COUPON.equals(discountType)) { // 금액할인
            return discountValue;
        }

        if (Promotion.DiscountType.CODE.equals(discountType)) { // 퍼센트할인
            return (int)((double)nowPrice / 100) * promotion.getDiscount_value();
        }
        throw new BusinessException(INVALID_PROMOTION_TYPE);

    }
    public boolean isValidPeriod(Promotion promotion) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(promotion.getUse_started_at()) && now.isBefore(promotion.getUse_ended_at());
    }

    public boolean isDuplicated(int[] array) {
        Set<Integer> set = new HashSet<>(Arrays.stream(array).boxed().toList());
        return array.length != set.size();
    }
}
