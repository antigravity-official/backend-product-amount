package antigravity.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import antigravity.config.P6spyConfiguration;

@DataJpaTest
@Import({P6spyConfiguration.class})
class PromotionJpaRepositoryTest {

	@Autowired
	private PromotionJpaRepository jpaRepository;

	@Test
	void findByIdAndProductId() {
		jpaRepository.findByIdAndProductId(1L, 1L);
	}
}