package antigravity.global;

import antigravity.domain.entity.Product;
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

    VALID_PROMOTION2("CODE", "15% 할인코드", "PERCENT", 15, LocalDate.parse("2022-11-01"), LocalDate.parse("2023-06-30"));

    private String promotionType; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discountType; // WON : 금액 할인, PERCENT : %할인
    private int discountValue; // 할인 금액 or 할인 %
    private LocalDate useStartedAt; // 쿠폰 사용가능 시작 기간
    private LocalDate useEndedAt; // 쿠폰 사용가능 종료 기간

    public Product toEntity() {
        return Product.of(
                name,
                price
        );
    }
}
