package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface CharacterService {
    ResponseModal<List<CharacterEntity>> getAllCharacter();

    ResponseModal<CharacterEntity> getCharacterById(Integer id);

}
