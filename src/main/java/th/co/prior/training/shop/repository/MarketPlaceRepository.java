package th.co.prior.training.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.shop.entity.MarketPlaceEntity;

import java.util.Optional;

@Repository
public interface MarketPlaceRepository extends JpaRepository<MarketPlaceEntity,Integer> {

    Optional<MarketPlaceEntity> findMarketPlaceByInventoryId(Integer id);
}
