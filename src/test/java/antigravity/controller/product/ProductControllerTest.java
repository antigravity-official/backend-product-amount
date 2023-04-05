package antigravity.controller.product;

import antigravity.service.product.ProductService;
import antigravity.service.product.exception.ProductNotFoundException;
import antigravity.service.product.resource.GetProductAmountResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @MockBean
    ProductService productService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("상품 가격 추출")
    void getProductAmountTest_1() throws Exception {
        // given
        when(productService.getProductAmount(any()))
            .thenReturn(new GetProductAmountResource("name", 100_000, 50_000, 50_000));

        // when
        mockMvc.perform(get("/products/amount"))

        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("name", "name").exists())
        .andExpect(jsonPath("originPrice").value( 100_000))
        .andExpect(jsonPath("discountPrice").value(50_000))
        .andExpect(jsonPath("finalPrice").value(50_000));
    }

    @Test
    @DisplayName("상품 가격 추출시 해당 상품이 존재하지 않을 경우 400 에러")
    void getProductAmountTest_2() throws Exception {
        // given
        when(productService.getProductAmount(any()))
            .thenThrow(ProductNotFoundException.class);

        // when
        mockMvc.perform(get("/products/amount"))

        // then
        .andExpect(status().isBadRequest());
    }
}
