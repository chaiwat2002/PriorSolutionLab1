package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.service.implement.InboxServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class InboxController {

    private final InboxServiceImpl inboxService;

    @GetMapping("/inbox")
    public ResponseEntity<ResponseModal<List<InboxEntity>>> getInboxes() {
        ResponseModal<List<InboxEntity>> response = this.inboxService.getAllInbox();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
