package antigravity.domain.service;

import antigravity.domain.entity.Product;
import antigravity.domain.service.DiscountCalculationService;
import antigravity.exception.EntityNotFoundException;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DiscountCalculationServiceTests {

    @Autowired
    private DiscountCalculationService service;

    @Test
    public void testThatAppliesValidDiscount() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> promotionIds = Arrays.asList(1, 2);

        BigDecimal discount = service.calculateDiscountAmount(product.getPrice(), promotionIds);

        // Applying existing promotions to product 1 = 62250
        assertEquals(discount.intValue(), 62250.00);
    }

    @Test
    public void testThatAppliesValidDiscountsOnebyOne() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> firstPromotionId = Arrays.asList(1);
        List<Integer> secondPromotionId = Arrays.asList(2);

        BigDecimal firstDiscount = service.calculateDiscountAmount(product.getPrice(), firstPromotionId);
        BigDecimal secondDiscount = service.calculateDiscountAmount(product.getPrice(), secondPromotionId);

        assertEquals(firstDiscount.intValue(), 30000.00);
        assertEquals(secondDiscount.intValue(), 32250.00);
    }

    @Test
    public void testThatAppliesInvalidDiscount() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> promotionIds = Arrays.asList(-1, 0);

        assertThrows(EntityNotFoundException.class,
                () -> service.calculateDiscountAmount(product.getPrice(), promotionIds));

        List<Integer> emptyPromotionIds = Arrays.asList();
        assertThrows(EntityNotFoundException.class,
                () -> service.calculateDiscountAmount(product.getPrice(), promotionIds));
    }
}
