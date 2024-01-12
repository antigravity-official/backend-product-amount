package antigravity.domain.service;

import antigravity.domain.entity.Product;
import antigravity.exception.EntityIsInvalidException;
import antigravity.repository.PromotionRepository;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PromotionValidationServiceTests {

    @Autowired
    private PromotionValidationService service;

    @Autowired
    private PromotionRepository repository;

    private Product product;

    @BeforeEach
    public void init() {
        product = TestHelper.buildSampleProduct();
    }

    @Test
    public void testThatValidatesValidPromotions() {
        List<Integer> promotionIds = Arrays.asList(1, 2);

        assertTrue(service.validatePromotion(product.getId(), promotionIds));
    }

    @Test
    public void testThatValidatesAlreadyUsedPromotions() {
        List<Integer> promotionIds = Arrays.asList(1, 2);
        repository.updatePromotionUsedAt(promotionIds);

        assertThrows(EntityIsInvalidException.class,
                () ->service.validatePromotion(product.getId(), promotionIds));
    }

    @Test
    public void testThatValidatesInvalidPromotions() {
        List<Integer> invalidPromotionIds = Arrays.asList(-1, 0);

        assertThrows(EntityIsInvalidException.class,
                () -> service.validatePromotion(product.getId(), invalidPromotionIds));
    }
}