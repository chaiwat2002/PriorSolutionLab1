package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface MarketPlaceService {

    ResponseModal<List<MarketPlaceEntity>> getAllMarkerPlace();

    ResponseModal<MarketPlaceEntity> getMarketPlaceById(Integer id);

    void addMarketPlace(Integer characterId, Integer inventoryId, double price);

    ResponseModal<String> sellItem(Integer characterId, Integer itemId, double price);

    ResponseModal<String> buyItem(Integer characterId, Integer itemId, double price);
}