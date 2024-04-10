package th.co.prior.training.shop.service.inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.service.implement.InventoryServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllInventoryTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryUtils inventoryUtils;

    @Before
    public void setUp() {
        when(inventoryRepository.findAll()).thenReturn(List.of(new InventoryEntity(), new InventoryEntity()));
        when(inventoryUtils.toDTOList(anyList())).thenReturn(List.of(new InventoryModel(), new InventoryModel()));
    }

    @Test
    public void testGetAllInventory_ShouldReturnStatusOK(){
        ResponseModel<List<InventoryModel>> result = inventoryService.getAllInventory();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllInventory_ShouldReturnStatusNotFound(){
        when(inventoryRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<InventoryModel>> result = inventoryService.getAllInventory();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllInventory_ShouldThrowStatusInternalServerError(){
        when(inventoryRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<InventoryModel>> result = inventoryService.getAllInventory();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
