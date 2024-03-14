package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.service.InventoryService;
import th.co.prior.training.shop.service.implement.InventoryServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/inventory")
    public ResponseEntity<ResponseModal<List<InventoryEntity>>> getInventory(){
        ResponseModal<List<InventoryEntity>> response = this.inventoryService.getAllInventory();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<ResponseModal<InventoryEntity>> getInventoryById(@PathVariable String id){
        ResponseModal<InventoryEntity> response = this.inventoryService.getInventoryById(Integer.parseInt(id));
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
