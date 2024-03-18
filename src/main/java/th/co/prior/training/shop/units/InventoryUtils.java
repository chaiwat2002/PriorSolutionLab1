package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.service.implement.InventoryServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InventoryUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryUtils.class);
    private final EntityUtils entityUtils;
    private final MonsterUtils monsterUtils;
    private final CharacterUtils characterUtils;
    private final InventoryRepository inventoryRepository;

    public List<InventoryModel> toDTOList(List<InventoryEntity> inventory) {
        return inventory.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryModel toDTO(InventoryEntity inventory){
        InventoryModel dto = new InventoryModel();
        dto.setId(inventory.getId());
        dto.setItem(inventory.getName());
        dto.setOwner(inventory.getCharacter().getName());
        dto.setDroppedBy(inventory.getMonster().getName());
        dto.setMarketable(inventory.isOnMarket());

        return dto;
    }

    public List<InventoryEntity> findAllInventory(){
        return inventoryRepository.findAll();
    }

    public InventoryEntity findInventoryById(Integer id){
        return inventoryRepository.findById(id).orElse(null);
    }

    public InventoryEntity findInventoryByName(String name) { return inventoryRepository.findInventoryByName(name).orElse(null); }

    public void addInventory(String name, Integer characterId, Integer monsterId) {
        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            MonsterEntity monster = this.monsterUtils.findMonsterById(monsterId);

            if (this.entityUtils.hasEntity(character, monster)) {
                InventoryEntity inventory = new InventoryEntity();
                inventory.setName(name);
                inventory.setCharacter(character);
                inventory.setMonster(monster);
                inventoryRepository.save(inventory);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
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

    public void setOnMarket(InventoryEntity inventory, boolean value){
        try{
            inventory.setOnMarket(value);
            this.inventoryRepository.save(inventory);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }
}
