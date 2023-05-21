package antigravity.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private int price;

	private final static int MAX_AMOUNT = 10000000;
	private final static int MIN_AMOUNT = 10000;

	@Builder
	public Product(Long id, String name, Integer price) {
		validateName(name);
		validatePrice(price);
		this.id = id;
		this.name = name;
		this.price = price;
	}

	private void validatePrice(Integer price) {
		Assert.notNull(price, "상품 가격이 입력되지 않았습니다.");
		Assert.isTrue(price >= MIN_AMOUNT && price <= MAX_AMOUNT,
			String.format("상품 가격은 최소 %d원 최대 %d원 입니다.", MIN_AMOUNT, MAX_AMOUNT));
	}

	private void validateName(String name) {
		Assert.hasText(name, "상품 이름이 입력되지 않았습니다.");
		Assert.isTrue(name.length() <= 30, "상품 이름은 최대 30자입니다.");
	}

}
