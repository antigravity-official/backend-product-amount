package antigravity.repository.product;

import antigravity.domain.Product;
import antigravity.global.base.RepositoryTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.global.fixture.ProductFixture.VALID_PRODUCT1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("[Repository] ProductQueryRepository - Data Jpa Test")
class ProductQueryRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductQueryRepository productQueryRepository;

    @Test
    @DisplayName("[findById] JPA - findById를 정상적으로 호출해, Product를 리턴합니다.")
    void When_FindById_Expect_Product() throws Exception {
        //given
        Product product = VALID_PRODUCT1.toEntity();
        //when
        Product savedProduct = productRepository.save(product);
        Product foundProduct = productQueryRepository.findById(product.getId()).get();
        //then
        assertAll(
                () -> assertThat(savedProduct).isSameAs(foundProduct),
                () -> assertThat(savedProduct.getId()).isEqualTo(foundProduct.getId()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(foundProduct.getPrice()),
                () -> assertThat(savedProduct.getName()).isEqualTo(foundProduct.getName())
        );
    }
}
