package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.MarketPlaceService;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MarketPlaceServiceImpl implements MarketPlaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketPlaceServiceImpl.class);
    private final MarketPlaceRepository marketPlaceRepository;
    private final CharacterServiceImpl characterService;
    private final InventoryServiceImpl inventoryService;
    private final InboxServiceImpl inboxService;
    private final AccountServiceImpl accountService;

    @Override
    public ResponseModal<List<MarketPlaceEntity>> getAllMarkerPlace() {
        ResponseModal<List<MarketPlaceEntity>> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if(marketPlaces.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(marketPlaces);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModal<MarketPlaceEntity> getMarketPlaceById(Integer id) {
        ResponseModal<MarketPlaceEntity> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            MarketPlaceEntity marketPlaces = this.marketPlaceRepository.findById(id).orElseThrow(() -> new RuntimeException("Market Place not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(marketPlaces);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void addMarketPlace(Integer characterId, Integer inventoryId, double price) {
        try {
            CharacterEntity character = this.characterService.getCharacterById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(inventoryId).getData();

            if(Objects.nonNull(character) && Objects.nonNull(inventory)) {
                MarketPlaceEntity marketPlace = new MarketPlaceEntity();
                marketPlace.setCharacter(character);
                marketPlace.setInventory(inventory);
                marketPlace.setPrice(price);
                marketPlace.setSold(false);
                this.marketPlaceRepository.save(marketPlace);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    @Override
    public ResponseModal<String> sellItem(Integer characterId, Integer itemId, double price) {
        ResponseModal<String> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("Item not found in Inventory.");

        try {
            CharacterEntity character = this.characterService.getCharacterById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(itemId).getData();
            List<MarketPlaceEntity> marketPlaces = this.marketPlaceRepository.findAll();

            if(Objects.nonNull(character) && Objects.nonNull(inventory) && inventory.getCharacter().equals(character)) {
                if(this.checkItemIdIsNotEquals(marketPlaces, inventory)) {
                        this.addMarketPlace(character.getId(), inventory.getId(), price);

                        this.inboxService.addInbox(
                                characterId,
                                "Your " + inventory.getName() + " has been added to the market.");

                        result.setStatus(200);
                        result.setDescription("OK");
                        result.setData("Successfully added.");
                } else {
                    result.setStatus(400);
                    result.setDescription("Bad Request");
                    result.setData("You already have " + inventory.getName() + " on the market.");
                }
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModal<String> buyItem(Integer characterId, Integer itemId, double price) {
        ResponseModal<String> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");
        result.setData("There are no products for sale on the market.");

        try {
            MarketPlaceEntity marketPlaces = this.getMarketPlaceById(itemId).getData();
            CharacterEntity character = this.characterService.getCharacterById(characterId).getData();
            InventoryEntity inventory = this.inventoryService.getInventoryById(itemId).getData();

            if(Objects.nonNull(character) && Objects.nonNull(inventory) && Objects.nonNull(marketPlaces)) {
                if (!marketPlaces.isSold()) {
                    if (marketPlaces.getPrice() <= price) {
                        this.accountService.depositBalance(marketPlaces.getCharacter().getId(), price);
                        this.accountService.withdrawBalance(character.getId(), price);

                        this.inventoryService.changeOwner(character, inventory);

                        marketPlaces.setSold(true);
                        this.marketPlaceRepository.save(marketPlaces);

                        this.inboxService.addInbox(
                                marketPlaces.getCharacter().getId(),
                                "Your " + marketPlaces.getInventory().getName() + " has been sold.");

                        result.setStatus(200);
                        result.setDescription("OK");
                        result.setData("Successfully purchased a " + marketPlaces.getInventory().getName() + ".");
                    } else {
                        result.setStatus(400);
                        result.setDescription("Bad Request");
                        result.setData("Your balance is not enough.");
                    }
                } else {
                    result.setStatus(400);
                    result.setDescription("Bad Request");
                    result.setData("The " + marketPlaces.getInventory().getName() + " has already been sold.");
                }
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    private boolean checkItemIdIsNotEquals(List<MarketPlaceEntity> market, InventoryEntity inventory) {
        return Optional.ofNullable(market)
                .map(m -> m.isEmpty() || m.stream().anyMatch(e -> !e.getInventory().getId().equals(inventory.getId())))
                .orElse(false);
    }
}
