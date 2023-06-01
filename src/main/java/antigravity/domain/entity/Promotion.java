package antigravity.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

    /* static factory method */
    public static Promotion of(
            final String promotionType, //쿠폰 타입 (쿠폰, 코드)
            final String name,
            final String discountType, // WON : 금액 할인, PERCENT : %할인
            final int discountValue, // 할인 금액 or 할인 %
            final LocalDate useStartedAt, // 쿠폰 사용가능 시작 기간
            final LocalDate useEndedAt
    ) {
        return Promotion.builder()
                .promotionType(promotionType)
                .name(name)
                .discountType(discountType)
                .discountValue(discountValue)
                .useStartedAt(useStartedAt)
                .useEndedAt(useEndedAt)
                .build();
    }
}
