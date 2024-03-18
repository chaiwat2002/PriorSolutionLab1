package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.*;
import th.co.prior.training.shop.units.*;

import java.util.List;

@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final EntityUtils entityUtils;
    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketPlaceUtils marketPlaceUtils;
    private final CharacterUtils characterUtils;
    private final InventoryUtils inventoryUtils;
    private final InboxUtils inboxUtils;
    private final AccountUtils accountUtils;

    @Override
    public ResponseModel<List<MarketPlaceModel>> getAllMarkerPlace() {
        ResponseModel<List<MarketPlaceModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if (marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved markets information.");
                result.setData(this.marketPlaceUtils.toDTOList(marketPlaces));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Marketplace not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> getMarketPlaceById(Integer id) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found!");

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceRepository.findById(id).orElseThrow(() -> new NullPointerException("Marketplace not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved market information.");
            result.setData(this.marketPlaceUtils.toDTO(marketPlaces));
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
    public ResponseModel<InventoryModel> buyItem(Integer characterId, Integer itemId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceUtils.findMarketPlaceById(itemId);
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(marketPlaces.getInventory().getId());
            AccountEntity account = this.accountUtils.findAccountById(character.getAccount().getId());

            if (this.entityUtils.hasEntity(character, inventory)) {
                if (!marketPlaces.isSold()) {
                    if (marketPlaces.getPrice() <= account.getBalance()) {
                        this.accountUtils.depositBalance(marketPlaces.getCharacter().getId(), marketPlaces.getPrice());
                        this.accountUtils.withdrawBalance(character.getId(), marketPlaces.getPrice());

                        this.inventoryUtils.changeOwner(character, inventory);
                        this.inventoryUtils.setOnMarket(inventory, false);

                        marketPlaces.setSold(true);
                        this.marketPlaceRepository.save(marketPlaces);

                        this.inboxUtils.addInbox(
                                marketPlaces.getCharacter().getId(),
                                "Your " + marketPlaces.getInventory().getName() + " has been sold.");

                        result.setStatus(200);
                        result.setMessage("OK");
                        result.setDescription("Successfully purchased a " + marketPlaces.getInventory().getName() + ".");
                        result.setData(this.inventoryUtils.toDTO(marketPlaces.getInventory()));
                    } else {
                        result.setStatus(400);
                        result.setMessage("Bad Request");
                        result.setDescription("Your balance is insufficient.");
                    }
                } else {
                    result.setStatus(400);
                    result.setMessage("Bad Request");
                    result.setDescription("The " + marketPlaces.getInventory().getName() + " has already been sold.");
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("There are no products for sale on the market.");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> sellItem(Integer characterId, Integer itemId, double price) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(itemId);
            MarketPlaceEntity marketPlaces = this.marketPlaceUtils.findMarketPlaceByInventoryId(inventory.getId());

            if (this.marketPlaceUtils.hasOwner(character, inventory)) {
                if (this.marketPlaceUtils.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                    this.marketPlaceUtils.addMarketPlace(character.getId(), inventory.getId(), price);

                    this.inboxUtils.addInbox(
                            characterId,
                            "Your " + inventory.getName() + " has been added to the market.");

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("You have successfully added a " + inventory.getName() + " to your inventory.");
                    result.setData(this.marketPlaceUtils.toDTO(
                            this.marketPlaceUtils.findMarketPlaceByInventoryId(inventory.getId())));
                } else {
                    result.setStatus(400);
                    result.setMessage("Bad Request");
                    result.setDescription("You already have " + inventory.getName() + " on the market.");
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Item not found in Inventory.");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<MarketPlaceModel> deleteMarketPlace(Integer id) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.marketPlaceRepository.findById(id).orElseThrow(() -> new NullPointerException("Marketplace not found!"));
            this.marketPlaceRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Market data has been deleted.");
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