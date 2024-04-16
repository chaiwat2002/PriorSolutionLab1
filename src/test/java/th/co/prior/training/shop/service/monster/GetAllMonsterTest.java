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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllMonsterTest {

    @InjectMocks
    private MonsterService monsterService;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterUtils monsterUtils;

    @Before
    public void setUp() {
        when(monsterRepository.findAll()).thenReturn(List.of(new MonsterEntity(), new MonsterEntity()));
        when(monsterUtils.toDTOList(anyList())).thenReturn(List.of(new MonsterModel(), new MonsterModel()));
    }

    @Test
    public void testGetAllMonster_ShouldReturnStatusOK(){
        ResponseModel<List<MonsterModel>> result = monsterService.getAllMonster();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllMonster_ShouldReturnStatusNotFound(){
        when(monsterRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<MonsterModel>> result = monsterService.getAllMonster();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllMonster_ShouldThrowStatusInternalServerError(){
        when(monsterRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<MonsterModel>> result = monsterService.getAllMonster();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
