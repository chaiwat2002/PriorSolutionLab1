package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface MonsterService {

    ResponseModal<List<MonsterEntity>> getAllMonster();

    ResponseModal<MonsterEntity> getMonsterById(Integer id);

    ResponseModal<String> attackMonster(Integer characterId, String monsterName, Integer damage);
}
