package antigravity.domain.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionType {

    COUPON("쿠폰사용시 현금할인"), CODE("코드사용시 %할인");

    private final String description; // 설명

}
