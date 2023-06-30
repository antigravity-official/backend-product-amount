package antigravity.domain.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionType {

    COUPON("쿠폰 타입"), CODE("코드 타입");

    private final String description; //설명
}
