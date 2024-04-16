package th.co.prior.training.shop.service.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.kafka.utils.InboxKafkaUtils;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.component.utils.MarketPlaceUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.MarketPlaceModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.MarketPlaceService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SellIteMarketPlaceTest {

    @InjectMocks
    private MarketPlaceService marketPlaceService;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private MarketPlaceUtils marketPlaceUtils;
    @Mock
    private AccountUtils accountUtils;
    @Mock
    private InboxKafkaUtils inboxKafkaUtils;

    @Before
    public void setUp(){
        when(characterRepository.findById(any())).thenReturn(Optional.of(new CharacterEntity("cwpd", new LevelEntity())));
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new InventoryEntity("Sword", new CharacterEntity("cwpd", new LevelEntity()), new MonsterEntity())));
        when(inventoryRepository.save(any())).thenReturn(new InventoryEntity());
        doNothing().when(inboxKafkaUtils).execute(any());
        when(marketPlaceUtils.toDTO(any())).thenReturn(new MarketPlaceModel());
    }

    @Test
    public void testSellItemMarketPlace_ShouldReturnStatusCreated() {
        ResponseModel<MarketPlaceModel> result = marketPlaceService.sellItem(1, 1, 10000.00);

        assertThat(result.getName()).isEqualTo("Created");
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testSellItemMarketPlace_ShouldReturnStatusBadRequest() {
        when(characterRepository.findById(any())).thenReturn(Optional.of(new CharacterEntity("ion", new LevelEntity())));

        ResponseModel<MarketPlaceModel> result = marketPlaceService.sellItem(1, 1, 10000.00);

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testSellItemMarketPlace_ShouldReturnStatusNotFound() {
        ResponseModel<MarketPlaceModel> result = marketPlaceService.sellItem(1, 2, 10000.00);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testSellItemMarketPlace_ShouldThrowStatusInternalServerError() {
        when(characterRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<MarketPlaceModel> result = marketPlaceService.sellItem(1, 1, 10000.00);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }
}
