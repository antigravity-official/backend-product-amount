package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PromotionRepositoryTests {

    @Autowired
    private PromotionRepository repository;

    @Test
    public void testThatGetsPromotion() {
        Promotion expected = TestHelper.buildSamplePromotion();
        List<Integer> ids = new ArrayList<>(Arrays.asList(expected.getId()));
        Promotion actual = repository.getPromotion(ids).get(0);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetsInvalidPromotion() {
        Promotion expected = TestHelper.buildSamplePromotion();
        expected.setId(0);
        List<Integer> ids = new ArrayList<>(Arrays.asList(expected.getId()));

        List<Promotion> actual = repository.getPromotion(ids);
        assertTrue(actual.isEmpty());
    }
}
