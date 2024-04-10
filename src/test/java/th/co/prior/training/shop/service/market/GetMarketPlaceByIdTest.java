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
import th.co.prior.training.shop.service.implement.MarketPlaceServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetMarketPlaceByIdTest {

    @InjectMocks
    private MarketPlaceServiceImpl marketPlaceService;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private MarketPlaceUtils marketPlaceUtils;

    @Before
    public void setUp() {
        when(marketPlaceRepository.findById(any())).thenReturn(Optional.of(new MarketPlaceEntity()));
        when(marketPlaceUtils.toDTO(any())).thenReturn(new MarketPlaceModel());
    }


    @Test
    public void testGetMarketPlaceById_ShouldReturnStatusOK() {
        ResponseModel<MarketPlaceModel> result = marketPlaceService.getMarketPlaceById(1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testGetMarketPlaceById_ShouldReturnStatusNotFound() {
        when(marketPlaceRepository.findById(1)).thenReturn(Optional.empty());

        ResponseModel<MarketPlaceModel> result = marketPlaceService.getMarketPlaceById(1);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetMarketPlaceById_ShouldThrowStatusInternalServerError() {
        when(marketPlaceRepository.findById(1)).thenThrow(new ExceptionModel());

        ResponseModel<MarketPlaceModel> result = marketPlaceService.getMarketPlaceById(1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
