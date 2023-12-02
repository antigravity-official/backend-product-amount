package antigravity.domain.entity;

import antigravity.constants.DiscountType;
import antigravity.constants.PromotionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)

    @Enumerated(EnumType.STRING)
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인

    private int discount_value; // 할인 금액 or 할인 %

    private LocalDateTime use_started_at; // 쿠폰 사용가능 시작 기간

    private LocalDateTime use_ended_at; // 쿠폰 사용가능 종료 기간
}
