package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@NoArgsConstructor(access = PRIVATE)
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
}
