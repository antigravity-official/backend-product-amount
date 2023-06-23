package antigravity.domain.entity;

import antigravity.domain.enums.DiscountType;
import antigravity.domain.enums.PromotionType;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Promotion {
    private int id;
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private Date useStartedAt; // 쿠폰 사용가능 시작 기간
    private Date useEndedAt; // 쿠폰 사용가능 종료 기간
}
