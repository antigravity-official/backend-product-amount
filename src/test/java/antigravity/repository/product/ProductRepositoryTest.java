package antigravity.repository.product;

import antigravity.domain.Product;
import antigravity.global.base.RepositoryTestSupport;
import antigravity.global.fixture.ProductFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.global.fixture.ProductFixture.VALID_PRODUCT1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[Repository] ProductRepository - Data Jpa Test")
class ProductRepositoryTest extends RepositoryTestSupport {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("[save] DB에 Product를 저장한다.")
    void When_ValidParameterRequested_Expect_save() throws Exception {
        //given && when
        Product savedProduct = productRepository.save(VALID_PRODUCT1.toEntity());
        //then
        assertAll(
                () -> assertThat(savedProduct.getName()).isEqualTo("남성 보정 속옷"),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(158500)
        );
    }
}
