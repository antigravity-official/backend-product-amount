package antigravity.domain.service;

import antigravity.domain.entity.Product;
import antigravity.exception.EntityNotFoundException;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        int discount = service.calculateDiscountAmount(product.getPrice(), promotionIds);

        // Applying existing promotions to product 1 = 62250
        assertEquals(discount, 62250);
    }

    @Test
    public void testThatAppliesValidDiscountsOnebyOne() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> firstPromotionId = Arrays.asList(1);
        List<Integer> secondPromotionId = Arrays.asList(2);

        int firstDiscount = service.calculateDiscountAmount(product.getPrice(), firstPromotionId);
        int secondDiscount = service.calculateDiscountAmount(product.getPrice(), secondPromotionId);

        assertEquals(firstDiscount, 30000);
        assertEquals(secondDiscount, 32250);
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
