package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.request.AttackRequest;
import th.co.prior.training.shop.request.MonsterRequest;
import th.co.prior.training.shop.service.MonsterService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1")
public class MonsterController {

    private final MonsterService monsterService;

    @GetMapping("/monster")
    public ResponseEntity<ResponseModel<List<MonsterModel>>> getMonster() {
        ResponseModel<List<MonsterModel>> response = this.monsterService.getAllMonster();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/monster/{id}")
    public ResponseEntity<ResponseModel<MonsterModel>> getMonsterById(@PathVariable Integer id) {
        ResponseModel<MonsterModel> response = this.monsterService.getMonsterById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/monster/create")
    public ResponseEntity<ResponseModel<MonsterModel>> createMonster(@RequestBody MonsterRequest request) {
        ResponseModel<MonsterModel> response = this.monsterService.createMonster(request.getName(), request.getMaxHealth(), request.getDropItem());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/monster/attack")
    public ResponseEntity<ResponseModel<MonsterModel>> attackMonster(@RequestBody AttackRequest request) {
        ResponseModel<MonsterModel> response = this.monsterService.attackMonster(request.getCharacterId(), request.getMonsterName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/monster/update/{id}")
    public ResponseEntity<ResponseModel<MonsterModel>> updateMonster(@PathVariable Integer id, @RequestBody MonsterRequest request){
        ResponseModel<MonsterModel> response = this.monsterService.updateMonster(id, request.getName(), request.getMaxHealth(), request.getDropItem());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/monster/delete/{id}")
    public ResponseEntity<ResponseModel<MonsterModel>> deleteMonster(@PathVariable Integer id){
        ResponseModel<MonsterModel> response = this.monsterService.deleteMonster(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
