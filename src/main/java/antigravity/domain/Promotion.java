package antigravity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)
    private int id;

    @Column
    private String promotionType; //쿠폰 타입 (쿠폰, 코드)

    @Column
    private String name;

    @Column
    private String discountType; // WON : 금액 할인, PERCENT : %할인

    @Column
    private int discountValue; // 할인 금액 or 할인 %

    @Column
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간

    @Column
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
