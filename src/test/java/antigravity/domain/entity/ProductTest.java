package antigravity.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ProductTest {

	@Test
	@DisplayName("product 생성 성공")
	public void testProductConstructor() {
		Long id = 1L;
		String name = "Product";
		Integer price = 50000;

		Product product = Product.builder()
			.id(id)
			.name(name)
			.price(price)
			.build();

		Assertions.assertEquals(id, product.getId());
		Assertions.assertEquals(name, product.getName());
		Assertions.assertEquals(price, product.getPrice());
	}

	@ParameterizedTest
	@DisplayName("product 생성 실패 - name이 유효하지 않을 경우")
	@ValueSource(strings = {" ", ""})
	@NullSource
	public void testProductValidation_name(String name) {
		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Product.builder()
				.id(1L)
				.name(name)
				.price(5000)
				.build()
		);
	}

	@ParameterizedTest
	@DisplayName("product 생성 실패 - price가 유효하지 않을 경우")
	@ValueSource(ints = {500, 20000000, 0})
	@NullSource
	public void testProductValidation_price(Integer price) {

		Assertions.assertThrows(IllegalArgumentException.class, () ->
			Product.builder()
				.name("Product")
				.price(price)
				.build()
		);

	}
}
