package antigravity.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import annotation.MockMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@MockMvcTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("상품 가격 조회 시 성공")
    void When_getProductAmount_Then_Success() throws Exception {

        mockMvc.perform(get("/products/amount")).andExpect(status().isOk()).andExpect(jsonPath("name").value("피팅노드상품"))
            .andExpect(jsonPath("originPrice").value(215000)).andExpect(jsonPath("discountPrice").value(57750))
            .andExpect(jsonPath("finalPrice").value(157000));
    }

}