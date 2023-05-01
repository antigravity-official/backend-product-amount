package antigravity.util;

import antigravity.enums.product.CutStandard;

public class CalculationUtil { //계산 관련

	public static int cutAmount(final int price, final CutStandard standard) {
		final int number = standard.getNumber();
		return (price / number) * number;
	}

}

