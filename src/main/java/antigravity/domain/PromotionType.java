package antigravity.domain;

public enum PromotionType {
	CODE( "WON"),
	COUPON("PERCENT");

	final private String discountType;

	PromotionType(String discountType) {
		this.discountType = discountType;
	}

	public DiscountType getDiscountType(){
		return DiscountType.valueOf(this.discountType);
	}

}
