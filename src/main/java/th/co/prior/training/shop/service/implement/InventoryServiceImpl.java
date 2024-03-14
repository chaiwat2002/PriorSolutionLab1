package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.service.InventoryService;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepository inventoryRepository;
    private final CharacterServiceImpl characterService;
    private final MonsterServiceImpl monsterService;

    @Override
    public ResponseModal<List<InventoryEntity>> getAllInventory() {
        ResponseModal<List<InventoryEntity>> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<InventoryEntity> inventories = this.inventoryRepository.findAll();

            if(inventories.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(inventories);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModal<InventoryEntity> getInventoryById(Integer id) {
        ResponseModal<InventoryEntity> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            InventoryEntity inventories = this.inventoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Inventory not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(inventories);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void addInventory(String name, Integer characterId, Integer monsterId) {
        ResponseModal<String> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("Can't found Character or Monster!");

        try {
            CharacterEntity character = this.characterService.getCharacterById(characterId).getData();
            MonsterEntity monster = this.monsterService.getMonsterById(monsterId).getData();

            if(Objects.nonNull(character) && Objects.nonNull(monster)) {
                InventoryEntity inventory = new InventoryEntity();
                inventory.setName(name);
                inventory.setCharacter(character);
                inventory.setMonster(monster);
                inventoryRepository.save(inventory);

                result.setStatus(200);
                result.setDescription("OK");
                result.setData("You have successfully added inventory.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

    }

    public void changeOwner(CharacterEntity character, InventoryEntity inventory){
        try {
            inventory.setCharacter(character);
            this.inventoryRepository.save(inventory);

        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

}
