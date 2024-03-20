package th.co.prior.training.shop.units;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.repository.AccountRepository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class AccountUtils {

    private final AccountRepository accountRepository;

    public List<AccountModel> toDTOList(List<AccountEntity> account) {
        return account.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AccountModel toDTO(AccountEntity account){
        AccountModel dto = new AccountModel();
        dto.setId(account.getId());
        dto.setCustomerName(account.getCharacter().getName());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());

        return dto;
    }

    public List<AccountEntity> findAllAccount(){
        return accountRepository.findAll();
    }

    public Optional<AccountEntity> findAccountById(Integer id){
        return accountRepository.findById(id);
    }


    public Optional<AccountEntity> findAccountByCharacterId(Integer characterId){
        return this.accountRepository.findAccountByCharacterId(characterId);
    }

    public AccountEntity createAccount(CharacterEntity character) {
        AccountEntity account = new AccountEntity();
        account.setAccountNumber(this.getAccountNumber());
        account.setBalance(3000.00);
        account.setCharacter(character);
        return this.accountRepository.save(account);
    }

    public AccountEntity updateAccount(AccountEntity account, double balance) {
        account.setBalance(balance);
        return this.accountRepository.save(account);
    }

    public void deleteAccountById(Integer id) {
        this.accountRepository.deleteById(id);
    }


    public void depositBalance(MarketPlaceEntity marketPlace, AccountEntity account) {
            double total = this.formatDecimal(account.getBalance() + marketPlace.getPrice());
            account.setBalance(total);
            this.accountRepository.save(account);
    }

    public void withdrawBalance(MarketPlaceEntity marketPlace, AccountEntity account) {
            double total = this.formatDecimal(account.getBalance() - marketPlace.getPrice());
            account.setBalance(total);
            this.accountRepository.save(account);
    }

    public String getAccountNumber(){
        StringBuilder start = new StringBuilder();
        Random value = new Random();

        int count = 0;
        int n = 0;

        for (int i = 0; i < 19; i++) {
            if (count == 4) {
                start.append(" ");
                count = 0;
            } else {
                n = value.nextInt(10);
                start.append(n);
                count++;
            }
        }

        return start.toString();
    }

    public double formatDecimal(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        String balance = df.format(value);

        return Double.parseDouble(balance);
    }

}
