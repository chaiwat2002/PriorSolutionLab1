package th.co.prior.training.shop.service.market;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.kafka.utils.InboxKafkaUtils;
import th.co.prior.training.shop.component.utils.AccountUtils;
import th.co.prior.training.shop.component.utils.InventoryUtils;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InventoryEntity;
import th.co.prior.training.shop.entity.MarketPlaceEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InventoryModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.InventoryRepository;
import th.co.prior.training.shop.repository.MarketPlaceRepository;
import th.co.prior.training.shop.service.implement.MarketPlaceServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuyItemMarkerPlaceTest {

    @InjectMocks
    private MarketPlaceServiceImpl marketPlaceService;
    @Mock
    private MarketPlaceRepository marketPlaceRepository;
    @Mock
    private CharacterRepository characterRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private InventoryUtils inventoryUtils;
    @Mock
    private AccountUtils accountUtils;
    @Mock
    private InboxKafkaUtils inboxKafkaUtils;

    @Before
    public void setUp() {
        CharacterEntity character = new CharacterEntity();
        character.setAccount(new AccountEntity(10000.00, character));
        MarketPlaceEntity marketPlace = new MarketPlaceEntity(new InventoryEntity(), character, 5000.00);
        when(marketPlaceRepository.findById(1)).thenReturn(Optional.of(marketPlace));
        when(characterRepository.findById(any())).thenReturn(Optional.of(character));
        when(inventoryRepository.findById(any())).thenReturn(Optional.of(new InventoryEntity()));
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity(10000.00, new CharacterEntity())));
        when(accountUtils.depositBalance(marketPlace, character.getAccount())).thenReturn(10000.00);
        when(accountRepository.saveAll(anyList())).thenReturn(List.of(new AccountEntity(), new AccountEntity()));
        when(inventoryRepository.save(any())).thenReturn(new InventoryEntity());
        when(marketPlaceRepository.save(any())).thenReturn(new MarketPlaceEntity());
        doNothing().when(inboxKafkaUtils).execute(any());
        when(inventoryUtils.toDTO(new InventoryEntity())).thenReturn(new InventoryModel());
    }

    @Test
    public void testBuyItemMarketPlace_ShouldReturnStatusOK() {
        ResponseModel<InventoryModel> result = marketPlaceService.buyItem(1, 1);

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
    }

    @Test
    public void testBuyItemMarketPlace_ShouldReturnStatusBadRequest() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new AccountEntity(1000.00, new CharacterEntity())));

        ResponseModel<InventoryModel> result = marketPlaceService.buyItem(1, 1);

        assertThat(result.getName()).isEqualTo("Bad Request");
        assertThat(result.getStatus()).isEqualTo(400);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testBuyItemMarketPlace_ShouldReturnStatusNotFound() {
        ResponseModel<InventoryModel> result = marketPlaceService.buyItem(1, 2);

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testBuyItemMarketPlace_ShouldThrowStatusInternalServerError() {
        when(marketPlaceRepository.findById(any())).thenThrow(new ExceptionModel());

        ResponseModel<InventoryModel> result = marketPlaceService.buyItem(1, 1);

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }
}
