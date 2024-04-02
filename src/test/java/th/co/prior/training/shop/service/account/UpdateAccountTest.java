package th.co.prior.training.shop.service.account;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.service.implement.AccountServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateAccountTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private AccountUtils accountUtils;

    @Before
    public void setUp() {
        when(characterRepository.findById(any())).thenReturn(Optional.of(new CharacterEntity()));
        when(accountRepository.findById(1)).thenReturn(Optional.of(new AccountEntity()));
        when(accountUtils.toDTO(any())).thenReturn(new AccountModel());
    }


    @Test
    public void testUpdateAccount_ShouldReturnStatusOK() {
        ResponseModel<AccountModel> result = accountService.updateAccount(1, 4000);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testUpdateAccount_ShouldReturnStatusNotFound() {
        ResponseModel<AccountModel> result = accountService.updateAccount(2, 4000);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testUpdateAccount_ShouldThrowStatusInternalServerError() {
        when(accountRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<AccountModel> result = accountService.updateAccount(1, 4000);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
