package antigravity.domain.entity;

import antigravity.domain.entity.common.BaseEntity;
import antigravity.domain.entity.common.DiscountType;
import antigravity.domain.entity.common.PromotionType;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인

    private int discountValue; // 할인 금액 or 할인 %

    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promotion)) return false;

        Promotion promotion = (Promotion) o;
        return id!= null && id.equals(promotion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
