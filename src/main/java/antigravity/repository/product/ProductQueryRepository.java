package antigravity.repository.product;

import antigravity.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ProductQueryRepository extends JpaRepository<Product, Integer> {

}
