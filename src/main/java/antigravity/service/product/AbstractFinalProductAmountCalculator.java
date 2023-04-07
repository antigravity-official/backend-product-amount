package antigravity.service.product;

import antigravity.domain.product.Product;
import antigravity.domain.promotion.Promotion;

import java.util.List;

abstract public class AbstractFinalProductAmountCalculator {
    abstract long calculate(Product product, List<Promotion> promotions);

    public long getPromotionsAppliedPrice(Product product, List<Promotion> promotions) {
        validation(product, promotions);
        return calculate(product, promotions);
    }

    private void validation(Product product, List<Promotion> promotions) {
        if (product == null) {
            throw new IllegalArgumentException("product must not be null");
        }
        if (promotions == null) {
            throw new IllegalArgumentException("promotions must not be null");
        }
    }
}
