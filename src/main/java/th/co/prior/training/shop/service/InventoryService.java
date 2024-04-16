package th.co.prior.training.shop.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.component.utils.InventoryUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;
    private final InventoryUtils inventoryUtils;

    public ResponseModel<List<InventoryModel>> getAllInventory() {
        ResponseModel<List<InventoryModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Inventory not found!");

        try {
            List<InventoryEntity> inventory = this.inventoryRepository.findAll();

            if (inventory.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved inventories information.");
                result.setData(this.inventoryUtils.toDTOList(inventory));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<InventoryModel> getInventoryById(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();

        try {
            InventoryEntity inventory = this.inventoryRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved inventory information.");
            result.setData(this.inventoryUtils.toDTO(inventory));
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<List<InventoryModel>> getInventoryByCharacterId(Integer id) {
        ResponseModel<List<InventoryModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Inventory not found!");

        try {
            List<InventoryEntity> inventory = this.inventoryRepository.findInventoryByCharacterId(id);

            if (inventory.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved inventories information.");
                result.setData(this.inventoryUtils.toDTOList(inventory));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public ResponseModel<InventoryModel> createInventory(String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");
        result.setMessage("Item not found!");

        try {
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterRepository.findById(monsterId)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            if (monster.getDropItem().equalsIgnoreCase(name)) {
                InventoryEntity saved = this.inventoryRepository.save(new InventoryEntity(monster.getDropItem(), character, monster));

                result.setStatus(201);
                result.setName("Created");
                result.setMessage("Inventory data was successfully created.");
                result.setData(this.inventoryUtils.toDTO(saved));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while creating inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<InventoryModel> updateInventory(Integer id, String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");
        result.setMessage("Item not found!");

        try {
            InventoryEntity inventory = this.inventoryRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterRepository.findById(monsterId)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            if (monster.getDropItem().equals(name)) {
                inventory.setName(name);
                inventory.setCharacter(character);
                inventory.setMonster(monster);

                InventoryEntity saved = this.inventoryRepository.save(inventory);

                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Inventory information has been successfully updated.");
                result.setData(this.inventoryUtils.toDTO(saved));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while updating inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<InventoryModel> deleteInventory(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.inventoryRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            this.inventoryRepository.deleteById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Inventory information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            log.error("Error occurred while deleting inventory", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

}