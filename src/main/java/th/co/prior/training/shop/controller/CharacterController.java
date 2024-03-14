package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.service.implement.CharacterServiceImpl;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

@RestController
@AllArgsConstructor
public class CharacterController {

    private final CharacterServiceImpl characterService;

    @GetMapping("/character")
    public ResponseEntity<ResponseModal<List<CharacterEntity>>> getCharacterById(){
        ResponseModal<List<CharacterEntity>> response = this.characterService.getAllCharacter();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/character/{id}")
    public ResponseEntity<ResponseModal<CharacterEntity>> getCharacterById(@PathVariable String id){
        ResponseModal<CharacterEntity> response = this.characterService.getCharacterById(Integer.parseInt(id));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
