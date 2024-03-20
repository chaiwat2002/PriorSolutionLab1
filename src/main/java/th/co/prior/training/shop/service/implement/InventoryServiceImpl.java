package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.InventoryService;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.InventoryUtils;
import th.co.prior.training.shop.units.MonsterUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final CharacterUtils characterUtils;
    private final MonsterUtils monsterUtils;
    private final InventoryUtils inventoryUtils;

    @Override
    public ResponseModel<List<InventoryModel>> getAllInventory() {
        ResponseModel<List<InventoryModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Inventory not found!");

        try {
            List<InventoryEntity> inventory = this.inventoryUtils.findAllInventory();

            if(inventory.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved inventories information.");
                result.setData(this.inventoryUtils.toDTOList(inventory));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> getInventoryById(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();

        try {
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved inventory information.");
            result.setData(this.inventoryUtils.toDTO(inventory));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> createInventory(String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");
        result.setMessage("Item not found!");

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterUtils.findMonsterById(monsterId)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            if(monster.getDropItem().equalsIgnoreCase(name)) {
                InventoryEntity saved = this.inventoryUtils.createInventory(monster, character);

                result.setStatus(201);
                result.setName("Created");
                result.setMessage("Inventory data was successfully created.");
                result.setData(this.inventoryUtils.toDTO(saved));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> updateInventory(Integer id, String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");
        result.setMessage("Item not found!");

        try {
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            MonsterEntity monster = this.monsterUtils.findMonsterById(monsterId)
                    .orElseThrow(() -> new ExceptionModel("Monster not found!", 404));

            if(monster.getDropItem().equals(name)) {
                InventoryEntity saved = this.inventoryUtils.updateInventory(inventory, name, monster, character);

                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Inventory information has been successfully updated.");
                result.setData(this.inventoryUtils.toDTO(saved));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> deleteInventory(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.inventoryUtils.findInventoryById(id)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            this.inventoryUtils.deleteInventoryById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Inventory information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

}
