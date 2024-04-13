package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CharacterUtils {

    List<CharacterModel> toDTOList(List<CharacterEntity> character);

    CharacterModel toDTO(CharacterEntity character);

}
