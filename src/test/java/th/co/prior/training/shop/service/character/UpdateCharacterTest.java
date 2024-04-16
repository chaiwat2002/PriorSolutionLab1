package th.co.prior.training.shop.service.character;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.service.CharacterService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCharacterTest {

    @InjectMocks
    private CharacterService characterService;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUtils characterUtils;

    @Before
    public void setUp() {
        when(characterRepository.findById(1)).thenReturn(Optional.of(new CharacterEntity()));
        when(characterRepository.save(any())).thenReturn(new CharacterEntity());
        when(characterUtils.toDTO(any())).thenReturn(new CharacterModel());
    }


    @Test
    public void testUpdateCharacter_ShouldReturnStatusOK() {
        ResponseModel<CharacterModel> result = characterService.updateCharacter(1, "cwpd");

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testUpdateCharacter_ShouldReturnStatusBadRequest() {
        ResponseModel<CharacterModel> result = characterService.updateCharacter(1, "io");

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }
    @Test
    public void testUpdateCharacter_ShouldReturnStatusNotFound() {
        ResponseModel<CharacterModel> result = characterService.updateCharacter(2, "cwpd");

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testUpdateCharacter_ShouldThrowStatusInternalServerError() {
        when(characterRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<CharacterModel> result = characterService.updateCharacter(1, "cwpd");

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
