package antigravity.domain.entity;

import antigravity.exception.EntityIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Promotion {

    private static final Promotion EMPTY = new Promotion(0, "", "", "",
            0, LocalDate.MIN, LocalDate.MIN);

    @Id
    private int id;
    private String promotion_type; //쿠폰 타입 (쿠폰, 코드)
    private String name;
    private String discount_type; // WON : 금액 할인, PERCENT : %할인
    private int discount_value; // 할인 금액 or 할인 %
    private LocalDate use_started_at; // 쿠폰 사용가능 시작 기간
    private LocalDate use_ended_at; // 쿠폰 사용가능 종료 기간

    public int getId() {
        checkIfEmpty();
        return id;
    }

    public String getPromotion_Type() {
        checkIfEmpty();
        return promotion_type;
    }

    public String getName() {
        checkIfEmpty();
        return name;
    }

    public String getDiscount_Type() {
        checkIfEmpty();
        return discount_type;
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

    public String toString() {
        checkIfEmpty();
        return this.toString();
    }

    private void checkIfEmpty() {
        if (this == EMPTY) {
            throw new EntityIsEmptyException("Promotion entity is empty or uninitialized.");
        }
    }
}
