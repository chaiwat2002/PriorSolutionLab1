package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.service.InventoryService;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.EntityUtils;
import th.co.prior.training.shop.units.InventoryUtils;
import th.co.prior.training.shop.units.MonsterUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final EntityUtils entityUtils;
    private final CharacterUtils characterUtils;
    private final MonsterUtils monsterUtils;
    private final InventoryRepository inventoryRepository;
    private final InventoryUtils inventoryUtils;

    @Override
    public ResponseModel<List<InventoryModel>> getAllInventory() {
        ResponseModel<List<InventoryModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<InventoryEntity> inventory = this.inventoryRepository.findAll();

            if(inventory.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved inventories information.");
                result.setData(this.inventoryUtils.toDTOList(inventory));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Inventory not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> getInventoryById(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            InventoryEntity inventory = this.inventoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Inventory not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved inventory information.");
            result.setData(this.inventoryUtils.toDTO(inventory));
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
    public ResponseModel<InventoryModel> createInventory(String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            MonsterEntity monster = this.monsterUtils.findMonsterById(monsterId);

            if(this.entityUtils.hasEntity(character, monster)) {
                if(monster.getDropItem().equalsIgnoreCase(name)) {
                    InventoryEntity inventory = new InventoryEntity();
                    inventory.setName(monster.getDropItem());
                    inventory.setCharacter(character);
                    inventory.setMonster(monster);
                    InventoryEntity saved = inventoryRepository.save(inventory);

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("Inventory data was successfully created.");
                    result.setData(this.inventoryUtils.toDTO(saved));
                } else {
                    throw new NullPointerException("Item not found!");
                }
            } else {
                throw new NullPointerException("Character or Monster not found!");
            }
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
    public ResponseModel<InventoryModel> updateInventory(Integer id, String name, Integer characterId, Integer monsterId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            InventoryEntity inventory = this.inventoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Inventory not found!"));
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            MonsterEntity monster = this.monsterUtils.findMonsterById(monsterId);

            if(this.entityUtils.hasEntity(character, monster)) {
                if(monster.getDropItem().equals(name)) {
                    inventory.setName(name);
                    inventory.setCharacter(character);
                    inventory.setMonster(monster);
                    this.inventoryRepository.save(inventory);

                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("Inventory information has been successfully updated.");
                    result.setData(this.inventoryUtils.toDTO(inventory));
                } else {
                    throw new NullPointerException("Item not found!");
                }
            } else {
                throw new NullPointerException("Character or Monster not found!");
            }
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
    public ResponseModel<InventoryModel> deleteInventory(Integer id) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.inventoryRepository.findById(id).orElseThrow(() -> new NullPointerException("Inventory not found!"));
            this.inventoryRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Inventory information has been deleted.");
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

}