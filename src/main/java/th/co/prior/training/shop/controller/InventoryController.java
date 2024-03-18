package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.request.InventoryRequest;
import th.co.prior.training.shop.service.InventoryService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/inventory")
    public ResponseEntity<ResponseModel<List<InventoryModel>>> getInventory(){
        ResponseModel<List<InventoryModel>> response = this.inventoryService.getAllInventory();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<ResponseModel<InventoryModel>> getInventoryById(@PathVariable Integer id){
        ResponseModel<InventoryModel> response = this.inventoryService.getInventoryById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/inventory/create")
    public ResponseEntity<ResponseModel<InventoryModel>> createCharacter(@RequestBody InventoryRequest request){
        ResponseModel<InventoryModel> response = this.inventoryService.createInventory(request.getName(), request.getCharacterId(), request.getMonsterId());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/inventory/update/{id}")
    public ResponseEntity<ResponseModel<InventoryModel>> updateCharacter(@PathVariable Integer id, @RequestBody InventoryRequest request){
        ResponseModel<InventoryModel> response = this.inventoryService.updateInventory(id, request.getName(), request.getCharacterId(), request.getMonsterId());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/inventory/delete/{id}")
    public ResponseEntity<ResponseModel<InventoryModel>> deleteCharacter(@PathVariable Integer id){
        ResponseModel<InventoryModel> response = this.inventoryService.deleteInventory(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
