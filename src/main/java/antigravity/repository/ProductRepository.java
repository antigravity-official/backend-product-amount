package antigravity.repository;

import java.util.Optional;

import antigravity.domain.entity.Product;

public interface ProductRepository {

	Optional<Product> findById(Long aLong);

}
