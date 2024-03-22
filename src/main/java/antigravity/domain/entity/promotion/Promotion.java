package antigravity.domain.entity.promotion;

import antigravity.domain.entity.promotion.enums.DiscountType;
import antigravity.domain.entity.promotion.enums.PromotionType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    @Enumerated(EnumType.STRING)
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간
}
