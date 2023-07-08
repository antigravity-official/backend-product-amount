package antigravity.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PromotionType {
    COUPON("COUPON"),
    CODE("CODE");

    private final String type;
}
