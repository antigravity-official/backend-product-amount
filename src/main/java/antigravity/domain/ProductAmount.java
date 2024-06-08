package antigravity.domain;

import antigravity.domain.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductAmount {
    private static final int PRODUCT_MIN_PRICE = 1000;
    private static final int PRODUCT_MAX_PRICE = 10000000;

    private int id;  // 상품 id
    private String name; // 상품명

    private int originPrice; // 상품 기존 가격
    private int discountPrice; // 총 할인 금액 - 최대 상품 기존 가격
    private int finalPrice; // 확정 상품 가격 - 최소 0

    public ProductAmount(Product product) {
        id = product.getId();
        name = product.getName();
        originPrice = product.getPrice();
        finalPrice = product.getPrice();
    }

    public void discount(int price) {
        int discount = originPrice - price;
        discountPrice = Math.min(originPrice, discountPrice + discount);
        finalPrice = Math.max(0, finalPrice - discount);
    }

    public int getDisplayDiscountPrice() {
        return originPrice - getDisplayFinalPrice(); // 최종 가격에 맞춘 할인 금액 조정
    }

    public int getDisplayFinalPrice() {
        int convertedFinalPrice = roundDownToThousand(finalPrice);// 최종 가격 천원 단위 절삭

        if (convertedFinalPrice < PRODUCT_MIN_PRICE) return PRODUCT_MIN_PRICE;
        if (convertedFinalPrice > PRODUCT_MAX_PRICE) return PRODUCT_MAX_PRICE;

        return convertedFinalPrice;
    }

    private int roundDownToThousand(int price) {
        return (price / 1000) * 1000;
    }
}
