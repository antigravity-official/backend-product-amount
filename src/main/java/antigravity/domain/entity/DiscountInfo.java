package antigravity.domain.entity;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Embeddable
public class DiscountInfo {
    @Enumerated(STRING)
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private Double discount_value; // 할인 금액 or 할인 %
}
