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
import th.co.prior.training.shop.service.implement.MonsterServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateMonsterTest {

    @InjectMocks
    private MonsterServiceImpl monsterService;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterUtils monsterUtils;

    @Before
    public void setUp() {
        when(monsterRepository.findMonsterByDropItem("Sword")).thenReturn(Optional.of(new MonsterEntity()));
        when(monsterRepository.save(any())).thenReturn(new MonsterEntity());
        when(monsterUtils.toDTO(any())).thenReturn(new MonsterModel());
    }


    @Test
    public void testCreateMonster_ShouldReturnStatusOK() {
        ResponseModel<MonsterModel> result = monsterService.createMonster("Goku", 50000, "Magic sword");

        assertThat(result.getName()).isEqualTo("Created");
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testCreateMonster_ShouldReturnStatusBadRequest() {
        ResponseModel<MonsterModel> result = monsterService.createMonster("Goku", 50000, "Sword");

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testCreateMonster_ShouldThrowStatusInternalServerError() {
        when(monsterRepository.save(any())).thenThrow(new ExceptionModel());

        ResponseModel<MonsterModel> result = monsterService.createMonster("Goku", 50000, "Magic sword");

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
