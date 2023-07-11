package antigravity.global.base;

import antigravity.controller.ProductPriceController;
import antigravity.service.product.ProductPriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ProductPriceService productPriceService;

}
