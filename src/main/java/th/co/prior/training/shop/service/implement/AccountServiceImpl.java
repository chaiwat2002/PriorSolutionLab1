package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.service.AccountService;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    @Override
    public ResponseModal<List<AccountEntity>> getAllAccount() {
        ResponseModal<List<AccountEntity>> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<AccountEntity> accounts = this.accountRepository.findAll();

            if (accounts.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(accounts);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void depositBalance(Integer id, double balance) {
        try {
            AccountEntity account = this.accountRepository.findAccountByCharactersId(id).orElseThrow(() -> new RuntimeException("Can't deposit balance because character not found!"));

            account.setBalance(account.getBalance() + balance);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }

    }

    @Override
    public void withdrawBalance(Integer id, double balance) {
        try {
            AccountEntity account = this.accountRepository.findAccountByCharactersId(id).orElseThrow(() -> new RuntimeException("Can't withdraw balance because character not found!"));

            account.setBalance(account.getBalance() - balance);
            this.accountRepository.save(account);
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }

    }
}
