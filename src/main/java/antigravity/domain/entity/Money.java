package antigravity.domain.entity;

import lombok.Getter;

import java.math.BigDecimal;

public class Money {

    @Getter
    private BigDecimal amount;

    public Money(BigDecimal money) {
        this.amount = money;
    }

    public Money(Integer money) {
        this.amount = new BigDecimal(money);
    }

    public Money plus(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money minus(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money multiply(Money money) {
        return new Money(this.amount.multiply(money.amount));
    }

    public Money divide(Money money) {
        return new Money(this.amount.divide(money.amount));
    }
}
