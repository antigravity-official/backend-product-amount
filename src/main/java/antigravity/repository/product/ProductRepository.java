package antigravity.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.product.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
