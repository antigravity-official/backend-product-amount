package antigravity.util;

public class CalculationUtil { //계산 관련

	private static final int CUT_STANDARD_AMOUNT = 1000; //절삭 기준 금액

	public static int cutAmount(final int price) {
		return (price / CUT_STANDARD_AMOUNT) * 1000;
	}

}

