package th.co.prior.training.shop.service.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.component.kafka.utils.InboxKafkaUtils;
import th.co.prior.training.shop.component.utils.*;
import th.co.prior.training.shop.entity.*;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private final MarketPlaceRepository marketPlaceRepository;
    private final CharacterRepository characterRepository;
    private final InventoryRepository inventoryRepository;
    private final AccountRepository accountRepository;
    private final MarketPlaceUtils marketPlaceUtils;
    private final InventoryUtils inventoryUtils;
    private final AccountUtils accountUtils;
    private final InboxKafkaUtils inboxKafkaUtils;

    @Override
    public ResponseModel<List<MarketPlaceModel>> getAllMarkerPlace() {
        ResponseModel<List<MarketPlaceModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Market not found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if (marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved markets information.");
                result.setData(this.marketPlaceUtils.toDTOList(marketPlaces));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching market", e);
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
            MarketPlaceEntity marketPlaces = this.marketPlaceRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Marketplace not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved market information.");
            result.setData(this.marketPlaceUtils.toDTO(marketPlaces));
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching market", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackOn = ExceptionModel.class)
    public ResponseModel<InventoryModel> buyItem(Integer characterId, Integer itemId) {
        ResponseModel<InventoryModel> result = new ResponseModel<>();

        try {
            MarketPlaceEntity marketPlace = this.marketPlaceRepository.findById(itemId)
                    .orElseThrow(() -> new ExceptionModel("There are no products for sale on the market.", 404));
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            InventoryEntity inventory = this.inventoryRepository.findById(marketPlace.getInventory().getId())
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            AccountEntity buyer = this.accountRepository.findById(character.getAccount().getId())
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));
            AccountEntity seller = marketPlace.getCharacter().getAccount();

            if (!isSold(marketPlace) && checkBalance(marketPlace, buyer)) {
                double revenue = this.accountUtils.depositBalance(marketPlace, marketPlace.getCharacter().getAccount());
                double expenses = this.accountUtils.withdrawBalance(marketPlace, buyer);

                seller.setBalance(revenue);
                buyer.setBalance(expenses);
                inventory.setCharacter(character);
                inventory.setOnMarket(false);
                marketPlace.setSold(true);

                this.accountRepository.saveAll(Arrays.asList(seller, buyer));
                this.inventoryRepository.save(inventory);
                this.marketPlaceRepository.save(marketPlace);
                this.inboxKafkaUtils.execute(
                        new InboxEntity("Your " + marketPlace.getInventory().getName() + " has been sold.", marketPlace.getCharacter()));

                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully purchased a " + marketPlace.getInventory().getName() + ".");
                result.setData(this.inventoryUtils.toDTO(marketPlace.getInventory()));
            } else {
                result.setStatus(400);
                result.setName("Bad Request");
                result.setMessage(!isSold(marketPlace) ?
                        "The " + marketPlace.getInventory().getName() + " has already been sold." : "Your balance is insufficient.");
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while updating market", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackOn = ExceptionModel.class)
    public ResponseModel<MarketPlaceModel> sellItem(Integer characterId, Integer itemId, double price) {
        ResponseModel<MarketPlaceModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            InventoryEntity inventory = this.inventoryRepository.findById(itemId)
                    .orElseThrow(() -> new ExceptionModel("Inventory not found!", 404));
            this.marketPlaceRepository.findMarketPlaceByInventoryId(inventory.getId())
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have " + inventory.getName() + " on the market.", 400);
                    });

            if (hasOwner(character, inventory)) {
                    double balance = this.accountUtils.formatDecimal(price);

                    inventory.setOnMarket(true);

                    this.inventoryRepository.save(inventory);
                    this.inboxKafkaUtils.execute(
                            new InboxEntity("Your " + inventory.getName() + " has been added to the market.", character));
                    MarketPlaceEntity saved = this.marketPlaceRepository.save(new MarketPlaceEntity(inventory, character, balance));

                    result.setStatus(201);
                    result.setName("Created");
                    result.setMessage("You have successfully added a " + inventory.getName() + " to your inventory.");
                    result.setData(this.marketPlaceUtils.toDTO(saved));
            } else {
                result.setStatus(400);
                result.setName("Bad Request");
                result.setMessage("Item not found!");
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while creating market", e);
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
            this.marketPlaceRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Marketplace not found!", 404));
            this.marketPlaceRepository.deleteById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Market data has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            log.error("Error occurred while deleting market", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    private boolean hasOwner(CharacterEntity character, InventoryEntity inventory) {
        return Objects.nonNull(character) && Objects.nonNull(inventory) && inventory.getCharacter().equals(character);
    }

    private boolean checkBalance(MarketPlaceEntity marketPlace, AccountEntity account) {
        return marketPlace.getPrice() <= account.getBalance();
    }

    private boolean isSold(MarketPlaceEntity marketPlace) {
        return marketPlace.isSold();
    }
}
