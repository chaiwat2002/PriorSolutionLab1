package th.co.prior.training.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.shop.entity.MonsterEntity;

import java.util.Optional;

@Repository
public interface MonsterRepository extends JpaRepository<MonsterEntity, Integer> {

    Optional<MonsterEntity> findMonsterByName(String name);
}
