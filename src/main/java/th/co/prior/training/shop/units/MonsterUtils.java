package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.repository.MonsterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MonsterUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterUtils.class);
    private final MonsterRepository monsterRepository;

    public List<MonsterModel> toDTOList(List<MonsterEntity> monster) {
        return monster.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MonsterModel toDTO(MonsterEntity monster){
        MonsterModel dto = new MonsterModel();
        dto.setId(monster.getId());
        dto.setName(monster.getName());
        dto.setMaxHealth(monster.getMaxHealth());
        dto.setDropItem(monster.getDropItem());

        return dto;
    }

    public List<MonsterEntity> findAllMonster(){
        return monsterRepository.findAll();
    }

    public MonsterEntity findMonsterById(Integer id){
        return monsterRepository.findById(id).orElse(null);
    }

    public MonsterEntity findMonsterByName(String name) { return monsterRepository.findMonsterByName(name).orElse(null); }
}
