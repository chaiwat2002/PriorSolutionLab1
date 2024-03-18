package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.service.AccountService;
import th.co.prior.training.shop.units.AccountUtils;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.EntityUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final EntityUtils entityUtils;
    private final CharacterUtils characterUtils;
    private final AccountUtils accountUtils;
    private final AccountRepository accountRepository;

    @Override
    public ResponseModel<List<AccountModel>> getAllAccount() {
        ResponseModel<List<AccountModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<AccountEntity> account = this.accountRepository.findAll();

            if (account.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved accounts information.");
                result.setData(this.accountUtils.toDTOList(account));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Account not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> getAccountById(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            AccountEntity account = this.accountRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved account information.");
            result.setData(this.accountUtils.toDTO(account));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> createAccount(Integer characterId, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<AccountEntity> duplicateAccount = this.accountRepository.findAccountByCharacterId(characterId);
            CharacterEntity character = this.characterUtils.findCharacterById(characterId);

            if(this.entityUtils.hasEntity(character)) {
                if(duplicateAccount.isEmpty()) {
                    AccountEntity account = new AccountEntity();
                    account.setAccountNumber(this.accountUtils.getAccountNumber());
                    account.setBalance(balance);
                    account.setCharacter(character);
                    AccountEntity saved = this.accountRepository.save(account);

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("Successfully created character information.");
                    result.setData(this.accountUtils.toDTO(saved));
                } else {
                    result.setDescription("You already have an account.");
                }
            } else {
                result.setDescription("Character not found.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> updateAccount(Integer id, double balance) {
        ResponseModel<AccountModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            AccountEntity account = this.accountRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));

            account.setBalance(balance);
            this.accountRepository.save(account);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Account information has been successfully updated.");
            result.setData(this.accountUtils.toDTO(account));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<AccountModel> deleteAccount(Integer id) {
        ResponseModel<AccountModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.accountRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));
            this.accountRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Account information has been successfully deleted.");
            result.setData(null);
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
