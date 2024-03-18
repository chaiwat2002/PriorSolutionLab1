package th.co.prior.training.shop.service;

import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface MonsterService {

    ResponseModel<List<MonsterModel>> getAllMonster();

    ResponseModel<MonsterModel> getMonsterById(Integer id);

    ResponseModel<MonsterModel> createMonster(String name, Integer maxHealth, String dropItem);


    ResponseModel<MonsterModel> updateMonster(Integer id, String name, Integer maxHealth, String dropItem);

    ResponseModel<MonsterModel> deleteMonster(Integer id);

    ResponseModel<MonsterModel> attackMonster(Integer characterId, String monsterName);
}
