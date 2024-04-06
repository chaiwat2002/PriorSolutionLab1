package th.co.prior.training.shop.component.utils.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.utils.MarketPlaceUtils;
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
public class MarketPlaceUtilsImpl implements MarketPlaceUtils {

    @Override
    public List<MarketPlaceModel> toDTOList(List<MarketPlaceEntity> marketPlace) {
        return marketPlace.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MarketPlaceModel toDTO(MarketPlaceEntity marketPlace) {
        MarketPlaceModel dto = new MarketPlaceModel();
        dto.setId(marketPlace.getId());
        dto.setItem(marketPlace.getInventory().getName());
        dto.setSeller(marketPlace.getCharacter().getName());
        dto.setPrice(marketPlace.getPrice());
        dto.setSoldStatus(marketPlace.isSold());

        return dto;
    }

}
