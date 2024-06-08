package antigravity.exception;

public class InvalidDiscountPriceException extends RuntimeException {
    private int price;

    public InvalidDiscountPriceException(int price) {
        super("유효하지 않은 할인 금액입니다. price = " + price);
    }
}
