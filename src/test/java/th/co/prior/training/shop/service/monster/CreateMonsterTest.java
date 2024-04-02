package th.co.prior.training.shop.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.LevelRepository;
import th.co.prior.training.shop.service.implement.CharacterServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateMonsterTest {

    @InjectMocks
    private CharacterServiceImpl characterService;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private LevelRepository levelRepository;
    @Mock
    private CharacterUtils characterUtils;

    @Before
    public void setUp() {
        when(characterRepository.findCharacterByName("cwpd")).thenReturn(Optional.of(new CharacterEntity()));
        when(levelRepository.findById(any())).thenReturn(Optional.of(new LevelEntity()));
        when(characterRepository.save(any())).thenReturn(new CharacterEntity());
        when(accountRepository.save(any())).thenReturn(new AccountEntity());
        when(characterUtils.toDTO(any())).thenReturn(new CharacterModel());
    }


    @Test
    public void testCreateCharacter_ShouldReturnStatusOK() {
        ResponseModel<CharacterModel> result = characterService.createCharacter("ion");

        assertThat(result.getName()).isEqualTo("Created");
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testCreateCharacter_ShouldReturnStatusBadRequest() {
        ResponseModel<CharacterModel> result = characterService.createCharacter("cwpd");

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testCreateCharacter_ShouldThrowStatusInternalServerError() {
        when(levelRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<CharacterModel> result = characterService.createCharacter("ion");

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
