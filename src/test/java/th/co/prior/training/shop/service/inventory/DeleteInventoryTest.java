package th.co.prior.training.shop.service.inventory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.service.InventoryService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteInventoryTest {

    @InjectMocks
    private InventoryService inventoryService;
    @Mock
    private InventoryRepository inventoryRepository;

    @Before
    public void setUp() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new InventoryEntity()));
    }


    @Test
    public void testDeleteInventory_ShouldReturnStatusOK() {
        ResponseModel<InventoryModel> result = inventoryService.deleteInventory(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteInventory_ShouldReturnStatusNotFound() {
        ResponseModel<InventoryModel> result = inventoryService.deleteInventory(2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteInventory_ShouldThrowStatusInternalServerError() {
        when(inventoryRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<InventoryModel> result = inventoryService.deleteInventory(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
