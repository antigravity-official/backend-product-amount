package antigravity.controller;

import antigravity.exception.EntityIsInvalidException;
import antigravity.exception.EntityNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.ProductService;
import antigravity.testutils.TestHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ProductInfoRequest request;
    private ProductAmountResponse expectedResponse;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        request = TestHelper.buildSampleProductInfoRequest();
        expectedResponse = TestHelper.buildExpectedResponseFromSample();
    }

    @Test
    public void testThatGetsValidProductAmount() throws Exception {

        given(productService.getProductAmount(request)).willReturn(expectedResponse);
        mockMvc.perform(get("/products/amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestHelper.asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.originPrice").value(expectedResponse.getOriginPrice()))
                .andExpect(jsonPath("$.discountPrice").value(expectedResponse.getDiscountPrice()))
                .andExpect(jsonPath("$.finalPrice").value(expectedResponse.getFinalPrice()));
    }

    @Test
    public void testThatGetsValidProductAmountThenThrowsExceptionWhenTryingAgain() throws Exception {

        // First API Call
        given(productService.getProductAmount(request)).willReturn(expectedResponse);
        mockMvc.perform(get("/products/amount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestHelper.asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()))
                .andExpect(jsonPath("$.originPrice").value(expectedResponse.getOriginPrice()))
                .andExpect(jsonPath("$.discountPrice").value(expectedResponse.getDiscountPrice()))
                .andExpect(jsonPath("$.finalPrice").value(expectedResponse.getFinalPrice()));

        // Second API Call (Throws NestedServletException, which wraps EntityIsInvalidException)
        given(productService.getProductAmount(request)).willThrow(EntityIsInvalidException.class);
        Assertions.assertThatThrownBy(() -> mockMvc.perform(get("/products/amount"))
                .andExpect(status().isBadRequest()))
                .isExactlyInstanceOf(NestedServletException.class)
                .hasCauseExactlyInstanceOf(EntityIsInvalidException.class);
    }

    @Test
    public void testThatGetsProductAmountByInvalidProductThenThrowsException() throws Exception {

        given(productService.getProductAmount(ArgumentMatchers.any(ProductInfoRequest.class)))
                .willThrow(EntityNotFoundException.class);
        Assertions.assertThatThrownBy(() -> mockMvc.perform(get("/products/amount"))
                .andExpect(status().isBadRequest()))
                .isExactlyInstanceOf(NestedServletException.class)
                .hasCauseExactlyInstanceOf(EntityNotFoundException.class);
    }
}
