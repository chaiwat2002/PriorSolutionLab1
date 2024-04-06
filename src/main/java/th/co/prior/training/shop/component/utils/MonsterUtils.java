package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.MonsterModel;

import java.util.List;
import java.util.Optional;

public interface MonsterUtils {

    List<MonsterModel> toDTOList(List<MonsterEntity> monster);

    MonsterModel toDTO(MonsterEntity monster);

}
