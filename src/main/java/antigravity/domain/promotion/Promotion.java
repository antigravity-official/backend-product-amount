package antigravity.domain.promotion;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static antigravity.domain.promotion.DiscountType.WON;
import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Table(
    name = "promotion",
    indexes = {
        @Index(name = "promotion_idx_01", columnList = "use_started_at, use_ended_at")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "promotion_type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "discount_type", length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    @Column(name = "discount_value", nullable = false)
    private int discountValue; // 할인 금액 or 할인 %
    @Column(name = "use_started_at", nullable = false)
    private LocalDateTime useStartedAt; // 쿠폰 사용가능 시작 기간
    @Column(name = "use_ended_at", nullable = false)
    private LocalDateTime useEndedAt; // 쿠폰 사용가능 종료 기간
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionProducts> promotionProducts = new ArrayList<>();

    @Builder
    public Promotion(PromotionType promotionType,
                     String name,
                     DiscountType discountType,
                     int discountValue,
                     LocalDateTime useStartedAt,
                     LocalDateTime useEndedAt,
                     List<Long> productIds) {
        this.promotionType = promotionType;
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.useStartedAt = useStartedAt;
        this.useEndedAt = useEndedAt;
        this.promotionProducts.addAll(
                productIds.stream()
                        .map(id -> new PromotionProducts(id, this))
                        .collect(toList())
        );
    }

    public long apply(long price) {
        if(WON.equals(this.discountType)) {
            return price - discountValue;
        }
        return price - (long) (price * (discountValue * 0.01));
    }
}
