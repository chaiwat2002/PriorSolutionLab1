package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface CharacterService {
    ResponseModel<List<CharacterModel> > getAllCharacter();

    ResponseModel<CharacterModel> getCharacterById(Integer id);

    ResponseModel<CharacterModel> createCharacter(String name);

    ResponseModel<CharacterModel> updateCharacter(Integer id, String name);

    ResponseModel<CharacterModel> deleteCharacter(Integer id);
}
