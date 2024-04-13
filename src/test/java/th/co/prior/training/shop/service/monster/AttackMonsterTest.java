package th.co.prior.training.shop.service.monster;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.component.utils.MonsterUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.implement.MonsterServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttackMonsterTest {

    @InjectMocks
    private MonsterServiceImpl monsterService;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryUtils inventoryUtils;

    @Before
    public void setUp() {
        when(monsterRepository.findMonsterByName("Pikachu")).thenReturn(Optional.of(new MonsterEntity("Pikachu", 500, "Sword")));
        when(characterRepository.findById(any())).thenReturn(Optional.of(new CharacterEntity("cwpd", new LevelEntity(5000))));
        when(inventoryRepository.save(any())).thenReturn(new InventoryEntity());
        when(inventoryUtils.toDTO(any())).thenReturn(new InventoryModel());
    }

    @Test
    public void testAttackMonster_ShouldReturnStatusOK() {
        ResponseModel<Object> result = monsterService.attackMonster(1, "Pikachu");

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testAttackMonster_ShouldReturnStatusNotFound() {
        ResponseModel<Object> result = monsterService.attackMonster(1, "Goku");

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testAttackMonster_ShouldThrowStatusInternalServerError() {
        when(monsterRepository.findMonsterByName(any())).thenThrow(new ExceptionModel());

        ResponseModel<Object> result = monsterService.attackMonster(1, "Pikachu");

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
