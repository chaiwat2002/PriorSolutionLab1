package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.request.InboxRequest;
import th.co.prior.training.shop.service.InboxService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1/inbox")
public class InboxController {

    private final InboxService inboxService;

    @GetMapping("/")
    public ResponseEntity<ResponseModel<List<InboxModel>>> getInbox() {
        ResponseModel<List<InboxModel>> response = this.inboxService.getAllInbox();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<InboxModel>> getInboxById(@PathVariable Integer id){
        ResponseModel<InboxModel> response = this.inboxService.getInboxById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseModel<InboxModel>> createCharacter(@RequestBody InboxRequest request){
        ResponseModel<InboxModel> response = this.inboxService.createInbox(request.getCharacterId(), request.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel<InboxModel>> updateCharacter(@PathVariable Integer id, @RequestBody InboxRequest request){
        ResponseModel<InboxModel> response = this.inboxService.updateInbox(id, request.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<InboxModel>> deleteCharacter(@PathVariable Integer id){
        ResponseModel<InboxModel> response = this.inboxService.deleteInbox(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
