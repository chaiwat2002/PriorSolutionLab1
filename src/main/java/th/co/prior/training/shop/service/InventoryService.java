package th.co.prior.training.shop.service;

import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface InventoryService {

    ResponseModel<List<InventoryModel>> getAllInventory();

    ResponseModel<InventoryModel> getInventoryById(Integer id);

    ResponseModel<List<InventoryModel>> getInventoryByCharacterId(Integer id);

    ResponseModel<InventoryModel> createInventory(String name, Integer characterId, Integer monsterId);

    ResponseModel<InventoryModel> updateInventory(Integer id, String name, Integer characterId, Integer monsterId);

    ResponseModel<InventoryModel> deleteInventory(Integer id);
}
