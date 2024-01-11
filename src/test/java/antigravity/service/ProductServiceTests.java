package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceTests {

    @Autowired
    private ProductService service;

    @Test
    public void testThatGetsProductAmount() {

        ProductInfoRequest request = TestHelper.buildSampleProductInfoRequest();
        ProductAmountResponse expectedResponse = TestHelper.buildExpectedResponseFromSample();

        ProductAmountResponse actualResponse = service.getProductAmount(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }
}
