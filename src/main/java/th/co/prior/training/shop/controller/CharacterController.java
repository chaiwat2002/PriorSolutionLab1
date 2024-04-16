package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.request.CharacterRequest;
import th.co.prior.training.shop.service.CharacterService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1/character")
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping("/")
    public ResponseEntity<ResponseModel<List<CharacterModel>>> getCharacter(){
        ResponseModel<List<CharacterModel>> response = this.characterService.getAllCharacter();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<CharacterModel>> getCharacterById(@PathVariable Integer id){
        ResponseModel<CharacterModel> response = this.characterService.getCharacterById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseModel<CharacterModel>> createCharacter(@RequestBody CharacterRequest request){
        ResponseModel<CharacterModel> response = this.characterService.createCharacter(request.getName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel<CharacterModel>> updateCharacter(@PathVariable Integer id, @RequestBody CharacterRequest request){
        ResponseModel<CharacterModel> response = this.characterService.updateCharacter(id, request.getName());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<CharacterModel>> deleteCharacter(@PathVariable Integer id){
        ResponseModel<CharacterModel> response = this.characterService.deleteCharacter(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
