package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.MonsterService;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.InventoryUtils;
import th.co.prior.training.shop.units.MonsterUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MonsterServiceImpl<T> implements MonsterService {

    private final MonsterUtils monsterUtils;
    private final CharacterUtils characterUtils;
    private final InventoryUtils inventoryUtils;
    @Override
    public ResponseModel<List<MonsterModel>> getAllMonster() {
        ResponseModel<List<MonsterModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Monster not found!");

        try {
            List<MonsterEntity> monsters = this.monsterUtils.findAllMonster();

            if(monsters.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved monsters information.");
                result.setData(this.monsterUtils.toDTOList(monsters));
            }
        } catch (ExceptionModel e) {
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
            MonsterEntity monsters = this.monsterUtils.findMonsterById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved monster information.");
            result.setData(this.monsterUtils.toDTO(monsters));
        } catch (ExceptionModel e) {
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
            Optional<MonsterEntity> monster = this.monsterUtils.findMonsterByName(name);
            Optional<MonsterEntity> item = this.monsterUtils.findMonsterByDropItem(dropItem);

            if (monster.isPresent() || item.isPresent()) {
                throw new ExceptionModel("You already have an monster or drop item.", 400);
            }

            MonsterEntity saved = this.monsterUtils.createMonster(name, maxHealth, dropItem);

            result.setStatus(201);
            result.setName("Created");
            result.setMessage("Successfully created monster information.");
            result.setData(this.monsterUtils.toDTO(saved));
        } catch (ExceptionModel e) {
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
            MonsterEntity monster = this.monsterUtils.findMonsterById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster noy found!", 404));

            MonsterEntity saved = this.monsterUtils.updateMonster(monster, name, maxHealth, dropItem);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Monster information has been successfully updated.");
            result.setData(this.monsterUtils.toDTO(saved));

        } catch (ExceptionModel e) {
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
            this.monsterUtils.findMonsterById(id)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));
            this.monsterUtils.deleteMonsterById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Monster information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
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
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterUtils.findMonsterByName(monsterName)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

                if (monster.getMaxHealth() <= character.getLevel().getDamage()) {
                    InventoryEntity saved = this.inventoryUtils.createInventory(monster, character);

                    result.setStatus(200);
                    result.setName("OK");
                    result.setMessage("You have successfully killed the boss. You have received a " + monster.getDropItem());
                    result.setData(this.inventoryUtils.toDTO(saved));
                } else {
                    result.setStatus(200);
                    result.setName("OK");
                    result.setMessage("You have been killed by " + monster.getName() + ". Because the damage is not enough");
                    result.setData(this.monsterUtils.toDTO(monster));
                }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
