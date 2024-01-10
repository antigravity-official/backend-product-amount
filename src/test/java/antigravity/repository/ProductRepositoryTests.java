package antigravity.repository;

import antigravity.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    @Test
    public void testThatGetsProduct() {
        Product expected = buildSampleProduct();
        Product actual = repository.getProduct(expected.getId()).orElse(null);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testThatGetsInvalidProduct() {
        Product expected = buildSampleProduct();
        expected.setId(0);

        assertThrows(DataAccessException.class, () -> repository.getProduct(expected.getId()));
    }

    private Product buildSampleProduct() {
        return Product.builder()
                .id(1)
                .name("피팅노드상품")
                .price(new BigDecimal(215000))
                .build();
    }
}