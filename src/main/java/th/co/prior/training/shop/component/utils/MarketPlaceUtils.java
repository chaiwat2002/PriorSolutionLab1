package th.co.prior.training.shop.component.utils;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.repository.MarketPlaceRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class MarketPlaceUtils {

    private final EntityUtils entityUtils;
    private final MarketPlaceRepository marketPlaceRepository;
    private final InboxUtils inboxUtils;
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

    public Optional<MarketPlaceEntity> findMarketPlaceById(Integer id) {
        return this.marketPlaceRepository.findById(id);
    }

    public Optional<MarketPlaceEntity> findMarketPlaceByInventoryId(Integer id) {
        return this.marketPlaceRepository.findMarketPlaceByInventoryId(id);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public MarketPlaceEntity postItem(CharacterEntity character, InventoryEntity inventory, double price) {
        double balance = this.accountUtils.formatDecimal(price);
        this.inventoryUtils.setOnMarket(inventory, true);

        this.inboxUtils.addInbox(
                character,
                "Your " + inventory.getName() + " has been added to the market.");

        MarketPlaceEntity marketPlace = new MarketPlaceEntity();
        marketPlace.setCharacter(character);
        marketPlace.setInventory(inventory);
        marketPlace.setPrice(balance);
        return this.marketPlaceRepository.save(marketPlace);
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public void sellItem(CharacterEntity character, InventoryEntity inventory, MarketPlaceEntity marketPlace, AccountEntity account) {
        this.accountUtils.depositBalance(marketPlace, account);
        this.accountUtils.withdrawBalance(marketPlace, account);

        this.inventoryUtils.changeOwner(character, inventory);
        this.inventoryUtils.setOnMarket(inventory, false);

        marketPlace.setSold(true);
        this.marketPlaceRepository.save(marketPlace);

        this.inboxUtils.addInbox(
                marketPlace.getCharacter(),
                "Your " + marketPlace.getInventory().getName() + " has been sold.");
    }

    public void deleteMarketPlaceById(Integer id) {
        this.marketPlaceRepository.deleteById(id);
    }

    public boolean checkItemIdIsNotEquals(MarketPlaceEntity market, InventoryEntity inventory) {
        return !this.entityUtils.hasEntity(market) || !(market.getInventory().isOnMarket() && market.getInventory().getId().equals(inventory.getId()));
    }

    public boolean hasOwner(CharacterEntity character, InventoryEntity inventory) {
        return this.entityUtils.hasEntity(character, inventory) && inventory.getCharacter().equals(character);
    }
}
