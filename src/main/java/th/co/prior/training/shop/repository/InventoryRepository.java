package th.co.prior.training.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.shop.entity.InventoryEntity;

import java.util.List;
import java.util.Optional;


@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {

    List<InventoryEntity> findInventoryByCharacterId(Integer id);
    Optional<InventoryEntity> findInventoryByName(String name);
}
