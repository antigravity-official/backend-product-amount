package antigravity.domain.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType {

    WON("금액 할인"), PERCENT("비율 할인");

    private final String description; //설명
}
