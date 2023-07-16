package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
public class Promotion {
    private int id;
    private PromotionType promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간

    @RequiredArgsConstructor
    public enum PromotionType {
        COUPON("COUPON"),
        CODE("CODE");

        private final String type;
    }

    @RequiredArgsConstructor
    public enum DiscountType {
        WON("WON"),
        PERCENT("PERCENT");

        private final String type;
    }
}
