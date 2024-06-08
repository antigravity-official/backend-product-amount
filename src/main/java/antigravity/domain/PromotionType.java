package antigravity.domain;

import antigravity.exception.PromotionTypeNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum PromotionType {
    COUPON,
    CODE,
    ;

    public static PromotionType of(String code) {
        try {
            return PromotionType.valueOf(code);
        } catch (Exception e) {
            log.error("", e);
            throw new PromotionTypeNotFoundException(code);
        }
    }
}
