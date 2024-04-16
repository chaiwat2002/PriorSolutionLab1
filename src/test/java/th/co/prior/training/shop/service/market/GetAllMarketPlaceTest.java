package th.co.prior.training.shop.service.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.MarketPlaceUtils;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.MarketPlaceService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllMarketPlaceTest {

    @InjectMocks
    private MarketPlaceService marketPlaceService;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private MarketPlaceUtils marketPlaceUtils;

    @Before
    public void setUp() {
        when(marketPlaceRepository.findAll()).thenReturn(List.of(new MarketPlaceEntity(), new MarketPlaceEntity()));
        when(marketPlaceUtils.toDTOList(anyList())).thenReturn(List.of(new MarketPlaceModel(), new MarketPlaceModel()));
    }

    @Test
    public void testGetAllMarketPlace_ShouldReturnStatusOK(){
        ResponseModel<List<MarketPlaceModel>> result = marketPlaceService.getAllMarkerPlace();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllMarketPlace_ShouldReturnStatusNotFound(){
        when(marketPlaceRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<MarketPlaceModel>> result = marketPlaceService.getAllMarkerPlace();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllMarketPlace_ShouldThrowStatusInternalServerError(){
        when(marketPlaceRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<MarketPlaceModel>> result = marketPlaceService.getAllMarkerPlace();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
