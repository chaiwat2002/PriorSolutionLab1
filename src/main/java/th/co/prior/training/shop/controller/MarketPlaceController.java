package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.request.ItemRequest;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.MarketPlaceService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1")
public class MarketPlaceController {

    private final MarketPlaceService marketPlaceService;

    @GetMapping("/market")
    public ResponseEntity<ResponseModel<List<MarketPlaceModel>>> getMarketPlace(){
        ResponseModel<List<MarketPlaceModel>> response = this.marketPlaceService.getAllMarkerPlace();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/market/{id}")
    public ResponseEntity<ResponseModel<MarketPlaceModel>> getMarketPlaceById(@PathVariable Integer id){
        ResponseModel<MarketPlaceModel> response = this.marketPlaceService.getMarketPlaceById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/market/buy")
    public ResponseEntity<ResponseModel<InventoryModel>> buyItem(@RequestBody ItemRequest request) {
        ResponseModel<InventoryModel> response = marketPlaceService.buyItem(request.getCharacterId(), request.getItemId());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/market/sell")
    public ResponseEntity<ResponseModel<MarketPlaceModel>> sellItem(@RequestBody ItemRequest request){
        ResponseModel<MarketPlaceModel> response = marketPlaceService.sellItem(request.getCharacterId(), request.getItemId(), request.getPrice());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/market/delete/{id}")
    public ResponseEntity<ResponseModel<MarketPlaceModel>> deleteItem(@PathVariable Integer id){
        ResponseModel<MarketPlaceModel> response = marketPlaceService.deleteMarketPlace(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}