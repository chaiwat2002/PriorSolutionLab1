package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.AccountModel;

import java.util.List;
import java.util.Optional;

public interface AccountUtils {

    List<AccountModel> toDTOList(List<AccountEntity> account);

    AccountModel toDTO(AccountEntity account);

    double depositBalance(MarketPlaceEntity marketPlace, AccountEntity account);

    double withdrawBalance(MarketPlaceEntity marketPlace, AccountEntity account);

    double formatDecimal(double value);

}
