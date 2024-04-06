package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.InventoryService;
import th.co.prior.training.shop.service.MonsterService;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.component.utils.MonsterUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MonsterServiceImpl implements MonsterService {

    private final MonsterRepository monsterRepository;
    private final CharacterRepository characterRepository;
    private final InventoryRepository inventoryRepository;
    private final MonsterUtils monsterUtils;

    @Override
    public ResponseModel<List<MonsterModel>> getAllMonster() {
        ResponseModel<List<MonsterModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Monster not found!");

        try {
            List<MonsterEntity> monsters = this.monsterRepository.findAll();

            if (monsters.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved monsters information.");
                result.setData(this.monsterUtils.toDTOList(monsters));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> getMonsterById(Integer id) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        try {
            MonsterEntity monsters = this.monsterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved monster information.");
            result.setData(this.monsterUtils.toDTO(monsters));
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> createMonster(String name, Integer maxHealth, String dropItem) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        try {
            this.monsterRepository.findMonsterByName(name)
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have an monster.", 400);
                    });
            this.monsterRepository.findMonsterByDropItem(dropItem)
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have an drop item.", 400);
                    });

            MonsterEntity monster = new MonsterEntity();
            monster.setName(name);
            monster.setMaxHealth(maxHealth);
            monster.setDropItem(dropItem);

            MonsterEntity saved = this.monsterRepository.save(monster);

            result.setStatus(201);
            result.setName("Created");
            result.setMessage("Successfully created monster information.");
            result.setData(this.monsterUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            log.error("Error occurred while creating monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> updateMonster(Integer id, String name, Integer maxHealth, String dropItem) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        try {
            this.monsterRepository.findMonsterByName(name)
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have an monster.", 400);
                    });
            this.monsterRepository.findMonsterByDropItem(dropItem)
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have an drop item.", 400);
                    });
            MonsterEntity monster = this.monsterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            monster.setName(name);
            monster.setMaxHealth(maxHealth);
            monster.setDropItem(dropItem);

            MonsterEntity saved = this.monsterRepository.save(monster);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Monster information has been successfully updated.");
            result.setData(this.monsterUtils.toDTO(saved));

        } catch (ExceptionModel e) {
            log.error("Error occurred while updating monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> deleteMonster(Integer id) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        try {
            this.monsterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));
            this.monsterRepository.deleteById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Monster information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            log.error("Error occurred while deleting monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<Object> attackMonster(Integer characterId, String monsterName) {
        ResponseModel<Object> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterRepository.findMonsterByName(monsterName)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            if (monster.getMaxHealth() <= character.getLevel().getDamage()) {
                this.inventoryRepository.save(new InventoryEntity(monster.getDropItem(), character, monster));

                result.setStatus(200);
                result.setName("OK");
                result.setMessage("You have successfully killed the boss. You have received a " + monster.getDropItem());
                result.setData(this.monsterUtils.toDTO(monster));
            } else {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("You have been killed by " + monster.getName() + ". Because the damage is not enough");
                result.setData(this.monsterUtils.toDTO(monster));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while attacking monster", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
