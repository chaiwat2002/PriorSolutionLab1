package th.co.prior.training.shop.service.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.MarketPlaceService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteMarketPlaceTest {

    @InjectMocks
    private MarketPlaceService marketPlaceService;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;

    @Before
    public void setUp() {
        when(marketPlaceRepository.findById(1)).thenReturn(Optional.of(new MarketPlaceEntity()));
    }


    @Test
    public void testDeleteMarketPlace_ShouldReturnStatusOK() {
        ResponseModel<MarketPlaceModel> result = marketPlaceService.deleteMarketPlace(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteMarketPlace_ShouldReturnStatusNotFound() {
        ResponseModel<MarketPlaceModel> result = marketPlaceService.deleteMarketPlace(2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testDeleteMarketPlace_ShouldThrowStatusInternalServerError() {
        when(marketPlaceRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<MarketPlaceModel> result = marketPlaceService.deleteMarketPlace(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
