package antigravity.service;

import antigravity.exception.EntityIsInvalidException;
import antigravity.exception.EntityNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.testutils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceTests {

    @Autowired
    private ProductService service;

    private ProductInfoRequest request;
    private ProductAmountResponse expectedResponse;

    @BeforeEach
    public void init() {
        request = TestHelper.buildSampleProductInfoRequest();
        expectedResponse = TestHelper.buildExpectedResponseFromSample();
    }

    @Test
    public void testThatGetsProductAmount() {
        ProductAmountResponse actualResponse = service.getProductAmount(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testThatGetsProductAmountByPromotionOneByOneThenThrowsWhenUsedAgain() {
        request.setCouponIds(new int[]{1});
        ProductAmountResponse firstActualResponse = service.getProductAmount(request);
        request.setCouponIds(new int[]{2});
        ProductAmountResponse secondActualResponse= service.getProductAmount(request);

        assertEquals(expectedResponse.getDiscountPrice(),
                firstActualResponse.getDiscountPrice(), secondActualResponse.getDiscountPrice());

        request.setCouponIds(new int[]{1, 2});
        assertThrows(EntityIsInvalidException.class,
                () -> service.getProductAmount(request));
    }

    @Test
    public void testThatGetsInvalidProductAmountByNotExistingProduct() {
        request.setProductId(-1);

        assertThrows(DataAccessException.class,
                () -> service.getProductAmount(request));
    }

    @Test
    public void testThatGetsInvalidProductAmountByNotExistingPromotions() {
        request.setCouponIds(new int[]{-1, 0});

        assertThrows(EntityIsInvalidException.class,
                () -> service.getProductAmount(request));
    }
}
