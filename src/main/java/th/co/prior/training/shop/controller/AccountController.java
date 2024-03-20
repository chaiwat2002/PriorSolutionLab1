package th.co.prior.training.shop.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.request.AccountRequest;
import th.co.prior.training.shop.service.AccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/prior/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<ResponseModel<List<AccountModel>>> getAccount(){
        ResponseModel<List<AccountModel>> response = this.accountService.getAllAccount();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<AccountModel>> getAccountById(@PathVariable Integer id){
        ResponseModel<AccountModel> response = this.accountService.getAccountById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseModel<AccountModel>> createAccount(@RequestBody AccountRequest request){
        ResponseModel<AccountModel> response = this.accountService.createAccount(request.getCharacterId(), request.getBalance());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel<AccountModel>> updateAccount(@PathVariable Integer id, @RequestBody AccountRequest request){
        ResponseModel<AccountModel> response = this.accountService.updateAccount(id, request.getBalance());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<AccountModel>> deleteAccount(@PathVariable Integer id){
        ResponseModel<AccountModel> response = this.accountService.deleteAccount(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
