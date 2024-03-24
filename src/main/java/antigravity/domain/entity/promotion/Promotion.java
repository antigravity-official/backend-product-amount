package antigravity.domain.entity.promotion;

import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotion.enums.PromotionType;
import antigravity.exception.BizException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간


    public boolean isAvailableOn(LocalDate date) {
        if(date.isBefore(useStartedAt) || date.isAfter(useEndedAt)) {
            throw new BizException(this.name + " is not available on " + date);
        }
        return true;
    }

    public int getDiscountFrom(int price) {
        return this.discountType.calculateDiscountPrice(price, discountValue);
    }

    @Builder
    private Promotion(PromotionType promotionType, String name, DiscountType discountType, int discountValue, LocalDate useStartedAt, LocalDate useEndedAt) {
        this.promotionType = promotionType;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.useStartedAt = useStartedAt;
        this.useEndedAt = useEndedAt;
    }
}
