package th.co.prior.training.shop.service;

import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface AccountService {
    ResponseModel<List<AccountModel>> getAllAccount();

    ResponseModel<AccountModel> getAccountById(Integer id);

    ResponseModel<AccountModel> createAccount(Integer characterId, double balance);

    ResponseModel<AccountModel> updateAccount(Integer id, double balance);

    ResponseModel<AccountModel> deleteAccount(Integer id);
}
