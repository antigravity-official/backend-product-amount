package antigravity.domain.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiscountType {
    WON("WON"),
    PERCENT("PERCENT");

    private final String type;
}

