package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.entity.response.ItemReponse;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.service.implement.MarketPlaceServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class MarketPlaceController {

    private final MarketPlaceServiceImpl marketPlaceService;

    @GetMapping("/market")
    public ResponseEntity<ResponseModal<List<MarketPlaceEntity>>> getMarket(){
        ResponseModal<List<MarketPlaceEntity>> response = this.marketPlaceService.getAllMarkerPlace();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/market/sell")
    public ResponseEntity<ResponseModal<String>> sellItem(@RequestBody ItemReponse request){
        ResponseModal<String> response = marketPlaceService.sellItem(request.getCharacterId(), request.getItemId(), request.getPrice());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/market/buy")
    public ResponseEntity<ResponseModal<String>> buyItem(@RequestBody ItemReponse request) {
        ResponseModal<String> response = marketPlaceService.buyItem(request.getCharacterId(), request.getItemId(), request.getPrice());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
