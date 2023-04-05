package antigravity.domain.entity.promotion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Entity
@Table(name = "promotion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "promotion_type")
    @Enumerated(EnumType.STRING)
    private PromotionType promotionType; //쿠폰 타입 (쿠폰, 코드)
    @Column(name = "name")
    private String name;
    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // WON : 금액 할인, PERCENT : %할인
    @Column(name = "discount_value")
    private double discountValue; // 할인 금액 or 할인 %
    @Column(name = "use_started_at")
    private LocalDateTime useStartedAt; // 쿠폰 사용가능 시작 기간
    @Column(name = "use_ended_at")
    private LocalDateTime useEndedAt; // 쿠폰 사용가능 종료 기간
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PromotionProducts> promotionProducts = new ArrayList<>();

    public Promotion(PromotionType promotionType,
                     String name,
                     DiscountType discountType,
                     double discountValue,
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
}
