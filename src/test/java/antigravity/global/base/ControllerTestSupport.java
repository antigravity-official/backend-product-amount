package antigravity.global.base;


import antigravity.service.discount.DiscountService;
import antigravity.service.product.ProductService;
import antigravity.service.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected PromotionService promotionService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected DiscountService discountService;

}
