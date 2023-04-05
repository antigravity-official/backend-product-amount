package antigravity.repository.product;

import antigravity.domain.product.Product;
import antigravity.domain.product.ProductRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static antigravity.entity.product.QProduct.product;

@Repository
public class QuerydslProductRepsoitory extends QuerydslRepositorySupport implements ProductRepository {
    public QuerydslProductRepsoitory() {
        super(Product.class);
    }

    @Override
    @Transactional
    public Optional<Product> findById(long productId) {
        return Optional.ofNullable(
                from(product)
                        .select(product)
                        .where(
                    product.id.eq(productId)
                ).fetchFirst()
        );
    }
}
