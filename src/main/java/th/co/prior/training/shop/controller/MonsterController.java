package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.entity.response.AttackResponse;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.service.implement.MonsterServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class MonsterController {

    private final MonsterServiceImpl monsterService;

    @GetMapping("/monster")
    public ResponseEntity<ResponseModal<List<MonsterEntity>>> getMonster() {
        ResponseModal<List<MonsterEntity>> response = this.monsterService.getAllMonster();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/attack")
    public ResponseEntity<ResponseModal<String>> attackBoss(@RequestBody AttackResponse request) {
        ResponseModal<String> response = this.monsterService.attackMonster(request.getCharacterId(), request.getMonsterName(), request.getDamage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
