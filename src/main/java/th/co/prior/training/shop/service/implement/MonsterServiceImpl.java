package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.MonsterService;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.EntityUtils;
import th.co.prior.training.shop.units.InventoryUtils;
import th.co.prior.training.shop.units.MonsterUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MonsterServiceImpl implements MonsterService {

    private final EntityUtils entityUtils;
    private final MonsterRepository monsterRepository;
    private final MonsterUtils monsterUtils;
    private final CharacterUtils characterUtils;
    private final InventoryUtils inventoryUtils;
    @Override
    public ResponseModel<List<MonsterModel>> getAllMonster() {
        ResponseModel<List<MonsterModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<MonsterEntity> monsters = this.monsterRepository.findAll();

            if(monsters.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved monsters information.");
                result.setData(this.monsterUtils.toDTOList(monsters));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Monster not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> getMonsterById(Integer id) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            MonsterEntity monsters = this.monsterRepository.findById(id).orElseThrow(() -> new NullPointerException("Monster not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved monster information.");
            result.setData(this.monsterUtils.toDTO(monsters));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> createMonster(String name, Integer maxHealth, String dropItem) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<MonsterEntity> duplicateMonsterName = this.monsterRepository.findMonsterByName(name);
            Optional<MonsterEntity> duplicateMonsterDropItem = this.monsterRepository.findMonsterByDropItem(dropItem);

            if(duplicateMonsterName.isEmpty() && duplicateMonsterDropItem.isEmpty()) {
                MonsterEntity monster = new MonsterEntity();
                monster.setName(name);
                monster.setMaxHealth(maxHealth);
                monster.setDropItem(dropItem);
                MonsterEntity saved = this.monsterRepository.save(monster);

                result.setStatus(201);
                result.setMessage("Created");
                result.setDescription("Successfully created monster information.");
                result.setData(this.monsterUtils.toDTO(saved));
            } else {
                    result.setDescription("You already have an monster or drop item.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> updateMonster(Integer id, String name, Integer maxHealth, String dropItem) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            MonsterEntity monster = this.monsterRepository.findById(id).orElseThrow(() -> new NullPointerException("Monster noy found!"));

            monster.setName(name);
            monster.setMaxHealth(maxHealth);
            monster.setDropItem(dropItem);
            this.monsterRepository.save(monster);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Monster information has been successfully updated.");
            result.setData(this.monsterUtils.toDTO(monster));

        } catch (NullPointerException e) {
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> deleteMonster(Integer id) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.monsterRepository.findById(id).orElseThrow(() -> new NullPointerException("Monster not found!"));
            this.monsterRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Monster information has been deleted.");
            result.setData(null);
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MonsterModel> attackMonster(Integer characterId, String monsterName) {
        ResponseModel<MonsterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            MonsterEntity monster = this.monsterUtils.findMonsterByName(monsterName);

            if (this.entityUtils.hasEntity(character, monster)) {
                if (monster.getMaxHealth() <= character.getLevel().getDamage()) {
                    this.inventoryUtils.addInventory(monster.getDropItem(), character.getId(), monster.getId());

                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("You have successfully killed the boss. You have received a " + monster.getDropItem());
                    result.setData(this.monsterUtils.toDTO(monster));
                } else {
                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("You have been killed by " + monster.getName() + ". Because the damage is not enough");
                    result.setData(this.monsterUtils.toDTO(monster));
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Can't found Character or Monster!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
