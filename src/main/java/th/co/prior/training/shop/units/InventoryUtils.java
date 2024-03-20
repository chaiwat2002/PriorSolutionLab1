package th.co.prior.training.shop.units;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryUtils {

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

    public Optional<InventoryEntity> findInventoryById(Integer id){
        return inventoryRepository.findById(id);
    }

    public Optional<InventoryEntity> findInventoryByName(String name) { return inventoryRepository.findInventoryByName(name); }

    @Transactional(rollbackOn = ExceptionModel.class)
    public InventoryEntity createInventory(MonsterEntity monster, CharacterEntity character) {
        InventoryEntity inventory = new InventoryEntity();
        inventory.setName(monster.getDropItem());
        inventory.setCharacter(character);
        inventory.setMonster(monster);
        return this.inventoryRepository.save(inventory);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public InventoryEntity updateInventory(InventoryEntity inventory, String name, MonsterEntity monster, CharacterEntity character) {
        inventory.setName(name);
        inventory.setCharacter(character);
        inventory.setMonster(monster);
        return this.inventoryRepository.save(inventory);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public void deleteInventoryById(Integer id) {
        this.inventoryRepository.deleteById(id);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public void changeOwner(CharacterEntity character, InventoryEntity inventory){
        inventory.setCharacter(character);
        this.inventoryRepository.save(inventory);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public void setOnMarket(InventoryEntity inventory, boolean value){
        inventory.setOnMarket(value);
        this.inventoryRepository.save(inventory);
    }

}
