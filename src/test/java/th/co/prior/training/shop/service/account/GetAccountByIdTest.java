package th.co.prior.training.shop.service.account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.AccountUtils;
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
public class GetAccountByIdTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountUtils accountUtils;

    @Before
    public void setUp() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity()));
        when(accountUtils.toDTO(any())).thenReturn(new AccountModel());
    }


    @Test
    public void testGetAccountById_ShouldReturnStatusOK() {
        ResponseModel<AccountModel> result = accountService.getAccountById(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetAccountById_ShouldReturnStatusNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        ResponseModel<AccountModel> result = accountService.getAccountById(1);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAccountById_ShouldThrowStatusInternalServerError() {
        when(accountRepository.findById(1)).thenThrow(new ExceptionModel());

        ResponseModel<AccountModel> result = accountService.getAccountById(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
