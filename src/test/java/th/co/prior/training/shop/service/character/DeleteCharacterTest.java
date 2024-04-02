package th.co.prior.training.shop.service.character;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.AccountModel;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.service.implement.AccountServiceImpl;
import th.co.prior.training.shop.service.implement.CharacterServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCharacterTest {

    @InjectMocks
    private CharacterServiceImpl characterService;
    @Mock
    private CharacterRepository characterRepository;

    @Before
    public void setUp() {
        when(characterRepository.findById(1)).thenReturn(Optional.of(new CharacterEntity()));
    }


    @Test
    public void testDeleteCharacter_ShouldReturnStatusOK() {
        ResponseModel<CharacterModel> result = characterService.deleteCharacter(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteCharacter_ShouldReturnStatusNotFound() {
        ResponseModel<CharacterModel> result = characterService.deleteCharacter(2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteCharacter_ShouldThrowStatusInternalServerError() {
        when(characterRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<CharacterModel> result = characterService.deleteCharacter(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
