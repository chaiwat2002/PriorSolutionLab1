package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.service.implement.AccountServiceImpl;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountServiceImpl accountService;

    @GetMapping("/account")
    public ResponseEntity<ResponseModal<List<AccountEntity>>> getAccount(){
        ResponseModal<List<AccountEntity>> response = this.accountService.getAllAccount();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
