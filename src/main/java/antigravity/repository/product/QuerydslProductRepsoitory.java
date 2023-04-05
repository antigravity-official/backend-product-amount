package antigravity.repository.product;

import antigravity.domain.product.Product;
import antigravity.domain.product.ProductRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static antigravity.entity.product.QProduct.product;

@Repository
@RequiredArgsConstructor
public class QuerydslProductRepsoitory implements ProductRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public Optional<Product> findById(long productId) {
        return Optional.ofNullable(
                jpaQueryFactory.select(product)
                        .from(product)
                        .where(
                            product.id.eq(productId)
                        ).fetchFirst()
        );
    }
}
