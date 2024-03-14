package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface AccountService {
    ResponseModal<List<AccountEntity>> getAllAccount();

    void depositBalance(Integer id, double balance);

    void withdrawBalance(Integer id, double balance);
}
