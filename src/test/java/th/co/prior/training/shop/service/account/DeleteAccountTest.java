package th.co.prior.training.shop.service.account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.service.AccountService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAccountTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(new AccountEntity()));
    }


    @Test
    public void testDeleteAccount_ShouldReturnStatusOK() {
        ResponseModel<AccountModel> result = accountService.deleteAccount(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteAccount_ShouldReturnStatusNotFound() {
        ResponseModel<AccountModel> result = accountService.deleteAccount(2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteAccount_ShouldThrowStatusInternalServerError() {
        when(accountRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<AccountModel> result = accountService.deleteAccount(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
