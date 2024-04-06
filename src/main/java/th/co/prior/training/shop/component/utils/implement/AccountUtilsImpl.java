package th.co.prior.training.shop.component.utils.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.repository.AccountRepository;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class AccountUtilsImpl implements AccountUtils {

    @Override
    public List<AccountModel> toDTOList(List<AccountEntity> account) {
        return account.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountModel toDTO(AccountEntity account){
        AccountModel dto = new AccountModel();
        dto.setId(account.getId());
        dto.setCustomerName(account.getCharacter().getName());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());

        return dto;
    }

    @Override
    public double depositBalance(MarketPlaceEntity marketPlace, AccountEntity account) {
            return this.formatDecimal(account.getBalance() + marketPlace.getPrice());
    }

    @Override
    public double withdrawBalance(MarketPlaceEntity marketPlace, AccountEntity account) {
           return this.formatDecimal(account.getBalance() - marketPlace.getPrice());
    }

    @Override
    public double formatDecimal(double value){
        DecimalFormat df = new DecimalFormat("#.##");
        String balance = df.format(value);

        return Double.parseDouble(balance);
    }

}
