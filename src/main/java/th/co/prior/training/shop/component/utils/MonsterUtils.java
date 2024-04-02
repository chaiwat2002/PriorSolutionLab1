package th.co.prior.training.shop.component.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.repository.MonsterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class MonsterUtils {

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
        return this.monsterRepository.findAll();
    }

    public Optional<MonsterEntity> findMonsterById(Integer id){
        return this.monsterRepository.findById(id);
    }

    public Optional<MonsterEntity> findMonsterByName(String name) { return this.monsterRepository.findMonsterByName(name); }

    public Optional<MonsterEntity> findMonsterByDropItem(String name) { return this.monsterRepository.findMonsterByDropItem(name); }

    public MonsterEntity createMonster(String name, Integer maxHealth, String dropItem) {
        MonsterEntity monster = new MonsterEntity();
        monster.setName(name);
        monster.setMaxHealth(maxHealth);
        monster.setDropItem(dropItem);
        return this.monsterRepository.save(monster);
    }

    public MonsterEntity updateMonster(MonsterEntity monster, String name, Integer maxHealth, String dropItem) {
        monster.setName(name);
        monster.setMaxHealth(maxHealth);
        monster.setDropItem(dropItem);
        return this.monsterRepository.save(monster);

    }

    public void deleteMonsterById(Integer id) {
        this.monsterRepository.deleteById(id);
    }
}
