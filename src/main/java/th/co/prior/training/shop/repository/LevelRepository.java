package th.co.prior.training.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.shop.entity.LevelEntity;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
}
