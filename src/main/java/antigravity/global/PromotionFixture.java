package antigravity.global;

import antigravity.domain.Promotion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum PromotionFixture {
    VALID_PROMOTION1(
            "COUPON",
            "30000원 할인쿠폰",
            "WON",
            30000,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2023-06-30")
    ),

    VALID_PROMOTION2(
            "CODE",
            "15% 할인코드",
            "PERCENT",
            15,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2023-06-30")
    ),

    VALID_PROMOTION3(
            "COUPON",
            "35% 할인코드",
            "PERCENT",
            35,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2023-06-30")
    ),

    VALID_PROMOTION4(
            "COUPON",
            "53000원 할인쿠폰",
            "WON",
            53000,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2023-06-30")
    ),

    EXPIRED_COUPON(
            "COUPON",
            "사용기간이 지난 쿠폰",
            "WON",
            53000,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2023-03-30")
    ),

    BEFORE_USAGE_PERIOD_COUPON(
            "COUPON",
            "사용기간이 도래하지 않은 쿠폰",
            "WON",
            53000,
            LocalDate.parse("2055-09-01"),
            LocalDate.parse("2056-03-30")
    ),

    UNKNOWN_TYPE_COUPON(
            "COUPON",
            "알 수 없는 타입의 쿠폰",
            "AntiGravity",
            53000,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2025-03-30")
    );

    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

    public Promotion toEntity() {
        return Promotion.of(
                promotionType,
                name,
                discountType,
                discountValue,
                useStartedAt,
                useEndedAt
        );
    }
}
