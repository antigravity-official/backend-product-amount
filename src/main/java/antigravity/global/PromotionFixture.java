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
            LocalDate.parse("2099-06-30")
    ),

    VALID_PROMOTION2(
            "CODE",
            "15% 할인코드",
            "PERCENT",
            15,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2099-06-30")
    ),

    VALID_PROMOTION3(
            "CODE",
            "35% 할인코드",
            "PERCENT",
            35,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2099-06-30")
    ),

    VALID_PROMOTION4(
            "COUPON",
            "53000원 할인쿠폰",
            "WON",
            53000,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2099-06-30")
    ),

    MINUS_PROMOTION(
            "COUPON",
            "-300000... 30만원 더 내세요!",
            "WON",
            -300000,
            LocalDate.parse("2022-11-01"),
            LocalDate.parse("2099-06-30")
    ),

    EXPIRED_PROMOTION(
            "COUPON",
            "사용기간이 지난 쿠폰",
            "WON",
            53000,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2023-03-30")
    ),

    BEFORE_USAGE_PERIOD_PROMOTION(
            "COUPON",
            "사용기간이 도래하지 않은 쿠폰",
            "WON",
            53000,
            LocalDate.parse("2055-09-01"),
            LocalDate.parse("2056-03-30")
    ),

    UNKNOWN_TYPE_PROMOTION(
            "COUPON",
            "알 수 없는 타입의 쿠폰",
            "AntiGravity",
            53000,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2025-03-30")
    ),

    SUPER_UPPER_PROMOTION(
            "COUPON",
            "130% 할인! 지금까지 이런 할인은 없었다!",
            "PERCENT",
            130,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2099-03-30")
    ),

    ZERO_PROMOTION(
            "COUPON",
            "0% 할인! 이건 무슨..이상한 쿠폰인가!?",
            "PERCENT",
            0,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2099-03-30")
    ),

    LOWER_PROMOTION(
            "COUPON",
            "-20% 할인! 오히려 비싸게 사세요!",
            "PERCENT",
            -20,
            LocalDate.parse("2022-09-01"),
            LocalDate.parse("2099-03-30")
    );

    private final String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private final String name;
    private final String discountType; // WON : 금액 할인, PERCENT : %할인
    private final int discountValue; // 할인 금액 or 할인 %
    private final LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private final LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

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
