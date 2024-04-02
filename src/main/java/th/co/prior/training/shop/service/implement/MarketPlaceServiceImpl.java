package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.component.utils.MarketPlaceUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.*;

import java.util.List;

@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final MarketPlaceUtils marketPlaceUtils;
    private final CharacterUtils characterUtils;
    private final InventoryUtils inventoryUtils;
    private final AccountUtils accountUtils;

    @Override
    public ResponseModel<List<MarketPlaceModel>> getAllMarkerPlace() {
        ResponseModel<List<MarketPlaceModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found!");
        result.setMessage("Market not found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceUtils.findAllMarketPlace();

            if (marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved markets information.");
                result.setData(this.marketPlaceUtils.toDTOList(marketPlaces));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> getMarketPlaceById(Integer id) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceUtils.findMarketPlaceById(id)
                    .orElseThrow(() -> new ExceptionModel("Marketplace not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved market information.");
            result.setData(this.marketPlaceUtils.toDTO(marketPlaces));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InventoryModel> buyItem(Integer characterId, Integer itemId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();

        try {
            MarketPlaceEntity marketPlace = this.marketPlaceUtils.findMarketPlaceById(itemId)
                    .orElseThrow(() -> new ExceptionModel("There are no products for sale on the market.", 400));
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(marketPlace.getInventory().getId())
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            AccountEntity account = this.accountUtils.findAccountById(character.getAccount().getId())
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

                if (!marketPlace.isSold()) {
                    if (marketPlace.getPrice() <= account.getBalance()) {
                        this.marketPlaceUtils.sellItem(character, inventory, marketPlace, account);

                        result.setStatus(200);
                        result.setName("OK");
                        result.setMessage("Successfully purchased a " + marketPlace.getInventory().getName() + ".");
                        result.setData(this.inventoryUtils.toDTO(marketPlace.getInventory()));
                    } else {
                        result.setMessage("Your balance is insufficient.");
                    }
                } else {
                    result.setMessage("The " + marketPlace.getInventory().getName() + " has already been sold.");
                }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> sellItem(Integer characterId, Integer itemId, double price) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(itemId)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            MarketPlaceEntity marketPlaces = this.marketPlaceUtils.findMarketPlaceByInventoryId(inventory.getId())
                    .orElse(null);

                if (this.marketPlaceUtils.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                    MarketPlaceEntity saved = this.marketPlaceUtils.postItem(character, inventory, price);

                    result.setStatus(201);
                    result.setName("Created");
                    result.setMessage("You have successfully added a " + inventory.getName() + " to your inventory.");
                    result.setData(this.marketPlaceUtils.toDTO(saved));
                } else {
                    result.setStatus(400);
                    result.setName("Bad Request");
                    result.setMessage("You already have " + inventory.getName() + " on the market.");
                }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> deleteMarketPlace(Integer id) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();

        try {
            this.marketPlaceUtils.findMarketPlaceById(id)
                    .orElseThrow(() -> new ExceptionModel("Marketplace not found!", 404));
            this.marketPlaceUtils.deleteMarketPlaceById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Market data has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}