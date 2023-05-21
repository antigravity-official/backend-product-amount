package antigravity.exception;

public class NotAvailableDatePromotionException extends RuntimeException {

	public NotAvailableDatePromotionException() {
		super("사용가능한 날짜의 프로모션이 아닙니다.");
	}
}
