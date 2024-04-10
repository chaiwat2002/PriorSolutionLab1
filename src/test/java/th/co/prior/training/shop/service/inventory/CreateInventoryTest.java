package th.co.prior.training.shop.service.inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.service.implement.InventoryServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateInventoryTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private InventoryUtils inventoryUtils;

    @Before
    public void setUp() {
        when(characterRepository.findById(1)).thenReturn(Optional.of(new CharacterEntity()));
        when(monsterRepository.findById(1)).thenReturn(Optional.of(new MonsterEntity("Pikachu", 30000, "Sword")));
        when(inventoryRepository.save(any())).thenReturn(new InventoryEntity());
        when(inventoryUtils.toDTO(any())).thenReturn(new InventoryModel());
    }


    @Test
    public void testCreateInventory_ShouldReturnStatusCreated() {
        ResponseModel<InventoryModel> result = inventoryService.createInventory("Sword", 1, 1);

        assertThat(result.getName()).isEqualTo("Created");
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testCreateInventory_ShouldReturnStatusBadRequest() {
        ResponseModel<InventoryModel> result = inventoryService.createInventory("Knife", 1, 1);

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testCreateInventory_ShouldReturnStatusNotFound() {
        when(characterRepository.findById(any())).thenReturn(Optional.empty());

        ResponseModel<InventoryModel> result = inventoryService.createInventory("Sword", 1, 1);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testCreateInventory_ShouldThrowStatusInternalServerError() {
        when(inventoryRepository.save(any())).thenThrow(new ExceptionModel());

        ResponseModel<InventoryModel> result = inventoryService.createInventory("Sword", 1, 1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
