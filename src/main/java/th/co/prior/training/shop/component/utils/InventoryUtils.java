package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.InventoryModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface InventoryUtils {

    List<InventoryModel> toDTOList(List<InventoryEntity> inventory);

    InventoryModel toDTO(InventoryEntity inventory);

}
