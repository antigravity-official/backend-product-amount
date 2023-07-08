package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class Promotion {
    private Integer id;
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private Money discount_value; // 할인 금액 or 할인 %
    private LocalDateTime use_started_at; // 쿠폰 사용가능 시작 기간
    private LocalDateTime use_ended_at; // 쿠폰 사용가능 종료 기간
}
