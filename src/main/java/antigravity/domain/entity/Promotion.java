package antigravity.domain.entity;

import antigravity.constants.DiscountType;
import antigravity.constants.PromotionType;
import antigravity.exception.EntityIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Promotion {

    private static final Promotion EMPTY = new Promotion(
            0, PromotionType.NONE, "", DiscountType.NONE,
            0, LocalDate.MIN, LocalDate.MIN, LocalDate.MIN);

    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private PromotionType promotion_type;  // 쿠폰 타입 (쿠폰, 코드)

    private String name;                   // 쿠폰 이름

    @Enumerated(EnumType.STRING)
    private DiscountType discount_type;    // WON : 금액 할인, PERCENT : %할인

    private int discount_value;            // 할인 금액 or 할인 %
    private LocalDate use_started_at;      // 쿠폰 사용가능 시작 기간
    private LocalDate use_ended_at;        // 쿠폰 사용가능 종료 기간
    private LocalDate used_at;             // 쿠폰 사용 확인용

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public PromotionType getPromotion_Type() {
        checkIfEmpty();
        return promotion_type;
    }

    public int getDiscount_Value() {
        checkIfEmpty();
        return discount_value;
    }

    public LocalDate getUse_Started_At() {
        checkIfEmpty();
        return use_started_at;
    }

    public LocalDate getUse_Ended_At() {
        checkIfEmpty();
        return use_ended_at;
    }

    public LocalDate getUsed_At() {
        checkIfEmpty();
        return used_at;
    }

    /**
     * Checks if the promotion has been used.
     *
     * @return true if the promotion has been used, false otherwise
     * @throws EntityIsEmptyException if the promotion is EMPTY
     */
    public boolean isUsed() {
        checkIfEmpty();
        return used_at != null;
    }

    /**
     * Checks if this instance is the same as EMPTY.
     *
     * @throws EntityIsEmptyException if this instance is EMPTY
     */
    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("Promotion entity is empty or uninitialized.");
        }
    }
}