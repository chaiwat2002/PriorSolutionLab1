package th.co.prior.training.shop.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.implement.MonsterServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteMonsterTest {

    @InjectMocks
    private MonsterServiceImpl monsterService;
    @Mock
    private MonsterRepository monsterRepository;

    @Before
    public void setUp() {
        when(monsterRepository.findById(1)).thenReturn(Optional.of(new MonsterEntity()));
    }


    @Test
    public void testDeleteCharacter_ShouldReturnStatusOK() {
        ResponseModel<MonsterModel> result = monsterService.deleteMonster(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteCharacter_ShouldReturnStatusNotFound() {
        ResponseModel<MonsterModel> result = monsterService.deleteMonster(2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteCharacter_ShouldThrowStatusInternalServerError() {
        when(monsterRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<MonsterModel> result = monsterService.deleteMonster(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
