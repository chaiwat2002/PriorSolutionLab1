package th.co.prior.training.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.prior.training.shop.entity.CharacterEntity;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Integer> {

    Optional<CharacterEntity> findCharacterByName(String name);

}
