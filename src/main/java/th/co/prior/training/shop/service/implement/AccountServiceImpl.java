package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.AccountService;
import th.co.prior.training.shop.units.AccountUtils;
import th.co.prior.training.shop.units.CharacterUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final CharacterUtils characterUtils;
    private final AccountUtils accountUtils;

    @Override
    public ResponseModel<List<AccountModel>> getAllAccount() {
        ResponseModel<List<AccountModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Account not found!");

        try {
            List<AccountEntity> account = this.accountUtils.findAllAccount();

            if (account.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved accounts information.");
                result.setData(this.accountUtils.toDTOList(account));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> getAccountById(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            AccountEntity account = this.accountUtils.findAccountById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved account information.");
            result.setData(this.accountUtils.toDTO(account));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> createAccount(Integer characterId, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(characterId)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            this.accountUtils.findAccountById(characterId)
                    .ifPresent(e -> { throw new ExceptionModel("You already have an account.", 400); });

            AccountEntity saved = this.accountUtils.createAccount(character);

            result.setStatus(201);
            result.setName("Created");
            result.setMessage("Successfully created character information.");
            result.setData(this.accountUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> updateAccount(Integer id, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            AccountEntity account = this.accountUtils.findAccountById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

            AccountEntity saved = this.accountUtils.updateAccount(account, balance);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Account information has been successfully updated.");
            result.setData(this.accountUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> deleteAccount(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();

        try {
            this.accountUtils.findAccountById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));
            this.accountUtils.deleteAccountById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Account information has been successfully deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
