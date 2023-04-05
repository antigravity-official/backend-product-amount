package antigravity.service.product;

import antigravity.domain.promotion.Promotion;

import java.util.Comparator;

import static antigravity.domain.promotion.DiscountType.WON;

public class PromotionApplyComparator implements Comparator<Promotion> {
    @Override
    public int compare(Promotion o1, Promotion o2) {
        return getScore(o2) - getScore(o1);
    }

    // 정액 쿠폰의 우선순위가 정률 쿠폰 보다 높음
    // 정률의 경우 percent가 100%를 초과하지는 않을 것 같지만 명시적으로 스코어를 부여하였음
    private int getScore(Promotion promotion) {
        if(WON.equals(promotion.getDiscountType())) {
            return 1_000 + promotion.getDiscountValue();
        }
        return promotion.getDiscountValue();
    }
}
