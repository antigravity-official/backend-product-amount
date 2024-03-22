package antigravity.repository;

import antigravity.domain.entity.promotionproducts.PromotionProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PromotionProductsRepositoryTest {

    @Autowired
    private PromotionProductsRepository repository;

    @DisplayName("JPA DB 연결 테스트")
    @Test
    void jpa_DB_연결_테스트() {
        //given
        /**
         * data.sql
         *
         * INSERT INTO promotion
         * VALUES (1, 'COUPON', '30000원 할인쿠폰', 'WON', 30000, '2022-11-01', '2023-03-01');
         * INSERT INTO promotion
         * VALUES (2, 'CODE', '15% 할인코드', 'PERCENT', 15, '2022-11-01', '2023-03-01');
         *
         * INSERT INTO product
         * VALUES (1, '피팅노드상품', 215000);
         *
         *
         * INSERT INTO promotion_products
         * VALUES (1, 1, 1);
         * INSERT INTO promotion_products
         * VALUES (2, 2, 1);
         */

        //when
        PromotionProducts promotionProducts = repository.findById(1).get();

        //then
        assertThat(promotionProducts.getProduct().getName()).isEqualTo("피팅노드상품");
        assertThat(promotionProducts.getPromotion().getName()).isEqualTo("30000원 할인쿠폰");

    }

}