package antigravity.model.response;

import antigravity.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductAmountResponseTest {
    @Test
    @DisplayName("천 원 단위로 절삭")
    void testFrom() {
        // Create a Product instance
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(5000);

        // Set the discount sum
        Integer discountSum = 2500;

        // Call the from method to create a ProductAmountResponse instance
        ProductAmountResponse response = ProductAmountResponse.from(product, discountSum);

        // Verify the values in the response
        assertEquals(2000, response.getFinalPrice());
    }
}