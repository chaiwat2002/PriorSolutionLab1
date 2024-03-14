package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface InventoryService {

    ResponseModal<List<InventoryEntity>> getAllInventory();

    ResponseModal<InventoryEntity> getInventoryById(Integer id);

    void addInventory(String name, Integer characterId, Integer monsterId);

}
