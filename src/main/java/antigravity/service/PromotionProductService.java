package antigravity.service;

import antigravity.constrants.PromotionConstrants;
import antigravity.domain.entity.Promotion;
import antigravity.mapper.PromotionProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PromotionProductService {

    private final PromotionProductMapper promotionProductMapper;

    public double getPromotionsDiscountPriceByProductId(int productId, int originPrice, int[] couponIds) throws ServerException {
        List<Promotion> promotionList = promotionProductMapper.getPromotionsByProductId(productId, couponIds);

        List<Integer> checkOverlap = new ArrayList<>();
        for (int couponId : couponIds) {
            if (checkOverlap.contains(couponId)) {
                throw new ServerException("중복된 프로모션은 적용 불가합니다.");
            } else {
                checkOverlap.add(couponId);
            }
        }

        // 유효하지 않은 프로모션이 있을 경우 무시하고 적용 할지, 에러를 낼지 정책 필요.
        // 우선 예외로 에러 처리.
        if (couponIds.length != promotionList.size()) {
            throw new ServerException("적용 프로모션 중 유효하지 않은 프로모션이 있습니다.");
        }

        AtomicBoolean invalidPromotion = new AtomicBoolean(false);
        double result = promotionList.stream().mapToDouble(p -> {
            // 쿠폰 할인 금액 추출
            if (PromotionConstrants.COUPON.getPromotionType().equals(p.getPromotionType())
                && PromotionConstrants.COUPON.getDiscountType().equals(p.getDiscountType())) {
                log.info("Applied COUPON Promotion Info: {}", p);
                log.info("Discount Amount: {}", p.getDiscountValue());

                return p.getDiscountValue();
            }
            // 코드 할인 금액 계산
            if (PromotionConstrants.CODE.getPromotionType().equals(p.getPromotionType())
                    && PromotionConstrants.CODE.getDiscountType().equals(p.getDiscountType())) {
                log.info("Applied CODE Promotion Info: {}", p);

                double discountRate = (p.getDiscountValue() / 100d);
                double discountAmount = (originPrice * discountRate);
                log.info("Discount Rate: {}%, Amount: {}", discountRate, discountAmount);

                return discountAmount;
            } else {
                log.error("Invalid Promotion Info: {}", p);
                invalidPromotion.set(true);
                return 0;
            }
        }).sum();

        if (invalidPromotion.get()) {
            throw new ServerException("적용 프로모션 중 유효하지 않은 프로모션이 있습니다.");
        }

        return result;
    }
}
