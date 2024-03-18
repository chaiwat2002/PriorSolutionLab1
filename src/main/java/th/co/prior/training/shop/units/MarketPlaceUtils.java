package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.repository.MarketPlaceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MarketPlaceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketPlaceUtils.class);
    private final EntityUtils entityUtils;
    private final MarketPlaceRepository marketPlaceRepository;
    private final CharacterUtils characterUtils;
    private final AccountUtils accountUtils;
    private final InventoryUtils inventoryUtils;

    public List<MarketPlaceModel> toDTOList(List<MarketPlaceEntity> marketPlace) {
        return marketPlace.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MarketPlaceModel toDTO(MarketPlaceEntity marketPlace) {
        MarketPlaceModel dto = new MarketPlaceModel();
        dto.setId(marketPlace.getId());
        dto.setItem(marketPlace.getInventory().getName());
        dto.setSeller(marketPlace.getCharacter().getName());
        dto.setPrice(marketPlace.getPrice());
        dto.setSoldStatus(marketPlace.isSold());

        return dto;
    }

    public List<MarketPlaceEntity> findAllMarketPlace() {
        return this.marketPlaceRepository.findAll();
    }

    public MarketPlaceEntity findMarketPlaceById(Integer id) {
        return this.marketPlaceRepository.findById(id).orElse(null);
    }

    public MarketPlaceEntity findMarketPlaceByInventoryId(Integer id) {
        return this.marketPlaceRepository.findMarketPlaceByInventoryId(id).orElse(null);
    }

    public void addMarketPlace(Integer characterId, Integer inventoryId, double price) {
        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);
            InventoryEntity inventory = this.inventoryUtils.findInventoryById(inventoryId);

            if(this.entityUtils.hasEntity(character, inventory)) {
                double balance = this.accountUtils.formatDecimal(price);

                MarketPlaceEntity marketPlace = new MarketPlaceEntity();
                marketPlace.setCharacter(character);
                marketPlace.setInventory(inventory);
                marketPlace.setPrice(balance);
                this.marketPlaceRepository.save(marketPlace);
                this.inventoryUtils.setOnMarket(inventory, true);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }

    public boolean checkItemIdIsNotEquals(MarketPlaceEntity market, InventoryEntity inventory) {
        return !this.entityUtils.hasEntity(market) || !(market.getInventory().isOnMarket() && market.getInventory().getId().equals(inventory.getId()));
    }

    public boolean hasOwner(CharacterEntity character, InventoryEntity inventory) {
        return this.entityUtils.hasEntity(character, inventory) && inventory.getCharacter().equals(character);
    }
}
