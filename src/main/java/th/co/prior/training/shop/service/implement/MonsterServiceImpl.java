package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.MonsterService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class MonsterServiceImpl implements MonsterService {

    private final MonsterRepository monsterRepository;
    private final CharacterServiceImpl characterService;
    private final InventoryServiceImpl inventoryService;
    @Override
    public ResponseModal<List<MonsterEntity>> getAllMonster() {
        ResponseModal<List<MonsterEntity>> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<MonsterEntity> monsters = this.monsterRepository.findAll();

            if(monsters.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(monsters);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModal<MonsterEntity> getMonsterById(Integer id) {
        ResponseModal<MonsterEntity> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            MonsterEntity monsters = this.monsterRepository.findById(id).orElseThrow(() -> new RuntimeException("Monster not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(monsters);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModal<String> attackMonster(Integer characterId, String monsterName, Integer damage) {
        ResponseModal<String> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("Can't found Character or Monster!");

        try {
            CharacterEntity character = this.characterService.getCharacterById(characterId).getData();
            Optional<MonsterEntity> monster = this.monsterRepository.findMonsterByName(monsterName);

            if(monster.isPresent() && Objects.nonNull(character)){
                if(monster.get().getHealth() < damage) {
                    this.inventoryService.addInventory(monster.get().getDropItem(), character.getId(), monster.get().getId());

                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData("You have successfully killed the boss. You have received a " + monster.get().getDropItem());
                } else {
                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData("You have been killed by " + monster.get().getName() + ". Because the damage is not enough");
                }
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
