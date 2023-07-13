package antigravity.global.base;

import antigravity.error.BusinessException;
import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected PromotionRepository promotionRepository;

    @Autowired
    protected PromotionQueryRepository promotionQueryRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ProductQueryRepository productQueryRepository;

    @Autowired
    protected PromotionProductsRepository promotionProductsRepository;

    @Autowired
    protected PromotionProductsQueryRepository promotionProductsQueryRepository;

    /**
     * @param thrown Exception
     *               던져진 예외가 BusinessException.class인지 검증하는 편의 메소드
     */
    protected void assertBusinessException(Exception exception) {
        assertEquals(requireNonNull(exception).getClass().getCanonicalName(), BusinessException.class.getCanonicalName());
    }

    /**
     * @param responseBody - 응답의 responseBody
     * @param expectedCode - 던져져야 하는 예외 코드
     * @throws JSONException -
     */
    protected void assertResponseBodyCode(String responseBody, String expectedCode) throws JSONException {
        JSONObject jsonObject = new JSONObject(responseBody);
        String actualCode = jsonObject.getString("code");
        assertEquals(expectedCode, actualCode);
    }

    protected Exception getException(MvcResult res) {
        return res.getResolvedException();
    }

    protected String getErrorCode(MvcResult res) {
        try {
            return res.getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected MockHttpServletRequestBuilder requestBuild(Integer productId, List<Integer> promotionIds, String BASE_URL) {
        return MockMvcRequestBuilders.get(BASE_URL)
                .param("productId", valueOf(productId))
                .param("promotionIds", collectionToCommaDelimitedString(promotionIds));
    }

}
