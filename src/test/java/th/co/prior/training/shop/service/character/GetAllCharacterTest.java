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
import th.co.prior.training.shop.service.implement.CharacterServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllCharacterTest {

    @InjectMocks
    private CharacterServiceImpl characterService;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private CharacterUtils characterUtils;

    @Before
    public void setUp() {
        when(characterRepository.findAll()).thenReturn(List.of(new CharacterEntity(), new CharacterEntity()));
        when(characterUtils.toDTOList(anyList())).thenReturn(List.of(new CharacterModel(), new CharacterModel()));
    }

    @Test
    public void testGetAllCharacter_ShouldReturnStatusOK(){
        ResponseModel<List<CharacterModel>> result = characterService.getAllCharacter();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllCharacter_ShouldReturnStatusNotFound(){
        when(characterRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<CharacterModel>> result = characterService.getAllCharacter();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllCharacter_ShouldThrowStatusInternalServerError(){
        when(characterRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<CharacterModel>> result = characterService.getAllCharacter();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
