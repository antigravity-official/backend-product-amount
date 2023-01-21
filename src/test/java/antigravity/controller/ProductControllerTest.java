package antigravity.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mock;

    @Test
    @Transactional
    @DisplayName("값이 제대로 출력 되는지 확인")
    void getProductAmount() throws Exception {
        /*
            INSERT INTO promotion VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
            INSERT INTO promotion VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2023-03-01');
            INSERT INTO product VALUES (1, '피팅노드상품', 215000);
         */
        String productName = "피팅노드상품";
        int originPrice = 215000;
        int couponDiscount = 30000;
        double codeDiscount = originPrice * (15 / 100d);
        double totalDiscount = couponDiscount + codeDiscount;
        int discountPrice = (int)(originPrice - totalDiscount);
        int finalPrice = discountPrice - (discountPrice % 1000);

        mock.perform(get("/products/amount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(productName)))
                .andExpect(jsonPath("$.originPrice", is(originPrice)))
                .andExpect(jsonPath("$.discountPrice", is(discountPrice)))
                .andExpect(jsonPath("$.finalPrice", is(finalPrice)))
                .andExpect(jsonPath("$.[?(@.finalPrice >= %d)]", 10000).exists())
                .andExpect(jsonPath("$.[?(@.finalPrice <= %d)]", 10000000).exists())
        ;
    }
}