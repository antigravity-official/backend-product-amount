package antigravity.domain.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.EntityNotFoundException;
import antigravity.repository.PromotionRepository;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private PromotionRepository repository;

    private Product product;

    @BeforeEach
    public void init() {
        product = TestHelper.buildSampleProduct();
    }

    @Test
    public void testThatAppliesValidDiscount() {
        List<Integer> promotionIds = Arrays.asList(1, 2);

        int discount = service.calculateDiscountAmount(product.getPrice(), promotionIds);

        // Applying existing promotions to product 1 = 62250
        assertEquals(discount, 62250);
    }

    @Test
    public void testThatAppliesValidDiscountsByPromotionOnebyOne() {
        List<Integer> firstPromotionId = Arrays.asList(1);
        List<Integer> secondPromotionId = Arrays.asList(2);

        int firstDiscount = service.calculateDiscountAmount(product.getPrice(), firstPromotionId);
        int secondDiscount = service.calculateDiscountAmount(product.getPrice(), secondPromotionId);

        assertEquals(firstDiscount, 30000);
        assertEquals(secondDiscount, 32250);
    }

    @Test
    public void testThatAppliesInvalidDiscountByInvalidPromotions() {
        List<Integer> promotionIds = Arrays.asList(-1, 0);

        assertThrows(EntityNotFoundException.class,
                () -> service.calculateDiscountAmount(product.getPrice(), promotionIds));

        List<Integer> emptyPromotionIds = Arrays.asList();
        assertThrows(EntityNotFoundException.class,
                () -> service.calculateDiscountAmount(product.getPrice(), promotionIds));
    }

    @Test
    public void testThatChecksIfAppliedPromotionsAreUsed() {
        List<Integer> promotionIds = Arrays.asList(1, 2);

        service.calculateDiscountAmount(product.getPrice(), promotionIds);

        List<Promotion> promotions = repository.getPromotion(promotionIds);
        promotions.stream()
                .forEach(promo -> assertTrue(promo.isUsed()));
    }
}
