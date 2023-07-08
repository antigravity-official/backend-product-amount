package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Promotion {
    private Integer id;
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    private Money discountValue; // 할인 금액 or 할인 %
    private LocalDateTime useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDateTime useEndedAt; // 쿠폰 사용가능 종료 기간
}
