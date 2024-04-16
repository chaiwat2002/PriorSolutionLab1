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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllAccountTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountUtils accountUtils;

    @Before
    public void setUp() {
        when(accountRepository.findAll()).thenReturn(List.of(new AccountEntity(), new AccountEntity()));
        when(accountUtils.toDTOList(anyList())).thenReturn(List.of(new AccountModel(), new AccountModel()));
    }

    @Test
    public void testGetAllAccount_ShouldReturnStatusOK(){
        ResponseModel<List<AccountModel>> result = accountService.getAllAccount();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllAccount_ShouldReturnStatusNotFound(){
        when(accountRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<AccountModel>> result = accountService.getAllAccount();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllAccount_ShouldThrowStatusInternalServerError(){
        when(accountRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<AccountModel>> result = accountService.getAllAccount();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
