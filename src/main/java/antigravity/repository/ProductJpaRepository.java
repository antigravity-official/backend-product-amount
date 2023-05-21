package antigravity.repository;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import antigravity.domain.entity.Product;

@Profile("dev")
public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {

	@Override
	Optional<Product> findById(Long aLong);
}
