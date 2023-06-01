package antigravity.repository;

import antigravity.domain.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    @Query("SELECT p FROM Promotion p WHERE p.id IN :promotionIds")
    List<Promotion> findPromotionsByIds(@Param("promotionIds") int[] promotionIds);
}
