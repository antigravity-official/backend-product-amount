package antigravity.service.product;

import antigravity.domain.product.Product;
import antigravity.domain.promotion.Promotion;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FinalProductAmountCalculator {
    public long getPromotionsAppliedPrice(Product product, List<Promotion> promotions) {
        long result = product.getPrice();
        Collections.sort(promotions, new PromotionApplyComparator());
        for (Promotion promotion : promotions) {
            result = promotion.apply(result);
            if (result < 10_000) {
                break;
            }
        }
        return getFinalAmount(product, result);
    }

    private long getFinalAmount(Product product, long result) {
        if (result == product.getPrice()) {
            return result;
        }
        int thousandUnit = (int) (result % 10_000);
        return result - thousandUnit;
    }
}
