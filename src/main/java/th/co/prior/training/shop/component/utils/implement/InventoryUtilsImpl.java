package th.co.prior.training.shop.component.utils.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.repository.InventoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class InventoryUtilsImpl implements InventoryUtils {

    @Override
    public List<InventoryModel> toDTOList(List<InventoryEntity> inventory) {
        return inventory.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryModel toDTO(InventoryEntity inventory){
        InventoryModel dto = new InventoryModel();
        dto.setId(inventory.getId());
        dto.setItem(inventory.getName());
        dto.setOwner(inventory.getCharacter().getName());
        dto.setDroppedBy(inventory.getMonster().getName());
        dto.setMarketable(inventory.isOnMarket());

        return dto;
    }

}
