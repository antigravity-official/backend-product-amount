package antigravity.domain;

public enum PromotionType {
	CODE( "PERCENT"),
	COUPON("WON");

	final private String discountType;

	PromotionType(String discountType) {
		this.discountType = discountType;
	}

	public DiscountType getDiscountType(){
		return DiscountType.valueOf(this.discountType);
	}

}
