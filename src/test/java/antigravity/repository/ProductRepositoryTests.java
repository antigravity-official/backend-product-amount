package antigravity.repository;

//import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@RequiredArgsConstructor
@SpringBootTest
@Transactional
public class ProductRepositoryTests {
    @Test
    public void testThatGetsProduct() {
        //STUB
        assertTrue(true);
    }
}