package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
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

    @Test
    public void testThatGetsPromotionProduct() {
        PromotionProducts expected = buildSamplePromotionProduct();
        PromotionProducts actual = repository.getPromotionProduct(expected.getId()).get(0);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetsInvalidPromotion() {
        PromotionProducts expected = buildSamplePromotionProduct();
        expected.setId(0);

        List<PromotionProducts> actual = repository.getPromotionProduct(expected.getId());
        assertTrue(actual.isEmpty());
    }

    private PromotionProducts buildSamplePromotionProduct() {
        return PromotionProducts.builder()
                .id(1)
                .promotionId(1)
                .productId(1)
                .build();
    }
}
