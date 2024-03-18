package th.co.prior.training.shop.service;

import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface MarketPlaceService {

    ResponseModel<List<MarketPlaceModel>> getAllMarkerPlace();

    ResponseModel<MarketPlaceModel> getMarketPlaceById(Integer id);

    ResponseModel<InventoryModel> buyItem(Integer characterId, Integer itemId);

    ResponseModel<MarketPlaceModel> sellItem(Integer characterId, Integer itemId, double price);

    ResponseModel<MarketPlaceModel> deleteMarketPlace(Integer id);
}