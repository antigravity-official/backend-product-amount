package antigravity.controller;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import antigravity.model.request.ProductInfoRequest;
import antigravity.service.ProductService;

@WebMvcTest(ProductController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProductControllerTest {

	@MockBean
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("유효한 요청 정보가 전달되면 메서드를 호출하고 200 OK를 반환한다.")
	void when_requestDataIsValid_expect_callMethodAndReturn200Ok() throws Exception {
		ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
			.productId(1)
			.couponIds(new int[] {1, 2})
			.build();

		ResultActions response =
			mockMvc.perform(
					MockMvcRequestBuilders.get("/products/amount")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(productInfoRequest)))
				.andDo(MockMvcResultHandlers.print());

		BDDMockito.verify(productService).getProductAmount(productInfoRequest);
		response.andExpect(MockMvcResultMatchers.status().isOk());
	}
}