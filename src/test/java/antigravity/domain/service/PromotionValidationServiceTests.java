package antigravity.domain.service;

import antigravity.domain.entity.Product;
import antigravity.exception.EntityIsInvalidException;
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
public class PromotionValidationServiceTests {

    @Autowired
    private PromotionValidationService service;

    @Test
    public void testThatValidatesValidPromotions() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> promotionIds = Arrays.asList(1, 2);

        assertTrue(service.validatePromotion(product.getId(), promotionIds));
    }

    @Test
    public void testThatValidatesAlreadyUsedPromotions() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> promotionIds = Arrays.asList(1, 2);

        assertTrue(service.validatePromotion(product.getId(), promotionIds));
    }

    @Test
    public void testThatValidatesInvalidPromotions() {
        Product product = TestHelper.buildSampleProduct();
        List<Integer> invalidPromotionIds = Arrays.asList(-1, 0);

        assertThrows(EntityIsInvalidException.class,
                () -> service.validatePromotion(product.getId(), invalidPromotionIds));
    }
}