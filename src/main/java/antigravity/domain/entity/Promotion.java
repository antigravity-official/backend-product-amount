package antigravity.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;

@Data
@Builder
public class Promotion {

    @Getter
    public enum DiscountType {
        COUPON("COUPON", "금액 할인"),
        CODE("CODE", "퍼센트 할인"),
        UNKNOWN("", ""),
        ;
        private final String code;
        private final String description;

        DiscountType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        public static DiscountType findEnumByCode(String code) {
            return Arrays.stream(values())
                    .filter(e -> e.code.equals(code))
                    .findAny().orElse(UNKNOWN);
        }
    }

    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private Date use_started_at; // 쿠폰 사용가능 시작 기간
    private Date use_ended_at; // 쿠폰 사용가능 종료 기간
}
