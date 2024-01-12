package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PromotionProductRepositoryTests {

    @Autowired
    private PromotionProductRepository repository;

    private PromotionProducts expected;

    @BeforeEach
    public void init() {
        expected = TestHelper.buildSamplePromotionProduct();
    }

    @Test
    public void testThatGetsPromotionProduct() {
        List<PromotionProducts> actual = repository.getPromotionProduct(expected.getId());

        assertFalse(actual.isEmpty());
        assertNotNull(actual.get(0));
        assertEquals(expected, actual.get(0));
    }

    @Test
    public void testThatGetsInvalidPromotion() {
        expected.setId(0);

        List<PromotionProducts> actual = repository.getPromotionProduct(expected.getId());
        assertTrue(actual.isEmpty());
    }
}
