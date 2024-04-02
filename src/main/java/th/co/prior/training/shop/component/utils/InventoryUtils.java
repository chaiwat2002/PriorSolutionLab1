package th.co.prior.training.shop.component.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryUtils {

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
        return this.inventoryRepository.findAll();
    }

    public Optional<InventoryEntity> findInventoryById(Integer id){
        return this.inventoryRepository.findById(id);
    }

    public List<InventoryEntity> findInventoryByCharacterId(Integer id) { return this.inventoryRepository.findInventoryByCharacterId(id); }

    public Optional<InventoryEntity> findInventoryByName(String name) { return this.inventoryRepository.findInventoryByName(name); }

    public InventoryEntity createInventory(MonsterEntity monster, CharacterEntity character) {
        InventoryEntity inventory = new InventoryEntity();
        inventory.setName(monster.getDropItem());
        inventory.setCharacter(character);
        inventory.setMonster(monster);
        return this.inventoryRepository.save(inventory);
    }

    public InventoryEntity updateInventory(InventoryEntity inventory, String name, MonsterEntity monster, CharacterEntity character) {
        inventory.setName(name);
        inventory.setCharacter(character);
        inventory.setMonster(monster);
        return this.inventoryRepository.save(inventory);
    }

    public void deleteInventoryById(Integer id) {
        this.inventoryRepository.deleteById(id);
    }

    public void changeOwner(CharacterEntity character, InventoryEntity inventory){
        inventory.setCharacter(character);
        this.inventoryRepository.save(inventory);
    }

    public void setOnMarket(InventoryEntity inventory, boolean value){
        inventory.setOnMarket(value);
        this.inventoryRepository.save(inventory);
    }

}
