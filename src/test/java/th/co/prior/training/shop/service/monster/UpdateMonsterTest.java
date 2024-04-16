package th.co.prior.training.shop.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.MonsterUtils;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.MonsterService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateMonsterTest {

    @InjectMocks
    private MonsterService monsterService;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterUtils monsterUtils;

    @Before
    public void setUp() {
        when(monsterRepository.findById(1)).thenReturn(Optional.of(new MonsterEntity()));
        when(monsterRepository.save(any())).thenReturn(new MonsterEntity());
        when(monsterUtils.toDTO(any())).thenReturn(new MonsterModel());
    }


    @Test
    public void testUpdateMonster_ShouldReturnStatusOK() {
        ResponseModel<MonsterModel> result = monsterService.updateMonster(1, "Goku", 5000, "Magic sword");

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testUpdateMonster_ShouldReturnStatusBadRequest() {
        when(monsterRepository.findMonsterByName("Pikachu")).thenReturn(Optional.of(new MonsterEntity()));

        ResponseModel<MonsterModel> result = monsterService.updateMonster(1, "Pikachu", 5000, "Sword");

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testUpdateMonster_ShouldReturnStatusNotFound() {
        ResponseModel<MonsterModel> result = monsterService.updateMonster(2, "Goku", 5000, "Magic sword");

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testUpdateMonster_ShouldThrowStatusInternalServerError() {
        when(monsterRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<MonsterModel> result = monsterService.updateMonster(1, "Goku", 5000, "Magic sword");

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
