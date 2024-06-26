package th.co.prior.training.shop.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService{

    private final AccountRepository accountRepository;
    private final CharacterRepository characterRepository;
    private final AccountUtils accountUtils;

    public ResponseModel<List<AccountModel>> getAllAccount() {
        ResponseModel<List<AccountModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Account not found!");

        try {
            List<AccountEntity> account = this.accountRepository.findAll();

            if (account.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved accounts information.");
                result.setData(this.accountUtils.toDTOList(account));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching account", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<AccountModel> getAccountById(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            AccountEntity account = this.accountRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved account information.");
            result.setData(this.accountUtils.toDTO(account));
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching account", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Transactional(rollbackOn = ExceptionModel.class)
    public ResponseModel<AccountModel> createAccount(Integer characterId, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterRepository.findById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            this.accountRepository.findById(characterId)
                    .ifPresent(e -> {
                        throw new ExceptionModel("You already have an account.", 400);
                    });

            AccountEntity saved = this.accountRepository.save(new AccountEntity(balance, character));

            result.setStatus(201);
            result.setName("Created");
            result.setMessage("Successfully created character information.");
            result.setData(this.accountUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            log.error("Error occurred while creating account", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<AccountModel> updateAccount(Integer id, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            AccountEntity account = this.accountRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

            account.setBalance(balance);
            AccountEntity saved = this.accountRepository.save(account);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Account information has been successfully updated.");
            result.setData(this.accountUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            log.error("Error occurred while updating account", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    public ResponseModel<AccountModel> deleteAccount(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            this.accountRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));
            this.accountRepository.deleteById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Account information has been successfully deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            log.error("Error occurred while deleting account", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

}
