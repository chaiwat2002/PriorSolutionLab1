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
public class GetCharacterByIdTest {

    @InjectMocks
    private CharacterService characterService;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUtils characterUtils;

    @Before
    public void setUp() {
        when(characterRepository.findById(any())).thenReturn(Optional.of(new CharacterEntity()));
        when(characterUtils.toDTO(any())).thenReturn(new CharacterModel());
    }


    @Test
    public void testGetCharacterById_ShouldReturnStatusOK() {
        ResponseModel<CharacterModel> result = characterService.getCharacterById(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetCharacterById_ShouldReturnStatusNotFound() {
        when(characterRepository.findById(1)).thenReturn(Optional.empty());

        ResponseModel<CharacterModel> result = characterService.getCharacterById(1);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetCharacterById_ShouldThrowStatusInternalServerError() {
        when(characterRepository.findById(1)).thenThrow(new ExceptionModel());

        ResponseModel<CharacterModel> result = characterService.getCharacterById(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
