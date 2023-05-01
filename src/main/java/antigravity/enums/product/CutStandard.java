package antigravity.enums.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CutStandard { //절삭 기준

	THOUSANDS_CUT_STANDARD(1000), //천 단위
	;

	private final int number; //기준 수

}
