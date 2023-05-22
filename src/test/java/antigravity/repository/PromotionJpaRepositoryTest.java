package antigravity.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import antigravity.config.P6spyConfiguration;

@DataJpaTest
@Import({P6spyConfiguration.class})
@DisplayName("jpa 쿼리 테스트")
class PromotionJpaRepositoryTest {

	@Autowired
	private PromotionJpaRepository jpaRepository;

	@Test
	void findByIdAndProductId() {
		Long[] ids = {1L, 2L};
		jpaRepository.findAllByIdsAndProductId(ids, 1L);
	}
}