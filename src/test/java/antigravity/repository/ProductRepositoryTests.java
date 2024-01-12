package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private Product expected;

    @BeforeEach
    public void init() {
        expected = TestHelper.buildSampleProduct();
    }

    @Test
    public void testThatGetsProduct() {
        Product actual = repository.getProduct(expected.getId()).orElse(null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetsInvalidProduct() {
        expected.setId(0);

        assertThrows(DataAccessException.class, () -> repository.getProduct(expected.getId()));
    }
}