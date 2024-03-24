package antigravity.repository;

import antigravity.domain.entity.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    List<Promotion> findAllByIdIn(int[] ids);
}
