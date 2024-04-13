package th.co.prior.training.shop.service.inbox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import th.co.prior.training.shop.component.utils.InboxUtils;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InboxRepository;
import th.co.prior.training.shop.service.implement.InboxServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetAllInboxTest {

    @InjectMocks
    private InboxServiceImpl inboxService;
    @Mock
    private InboxRepository inboxRepository;
    @Mock
    private InboxUtils inboxUtils;

    @Before
    public void setUp() {
        when(inboxRepository.findAll()).thenReturn(List.of(new InboxEntity(), new InboxEntity()));
        when(inboxUtils.toDTOList(anyList())).thenReturn(List.of(new InboxModel(), new InboxModel()));
    }

    @Test
    public void testGetAllInbox_ShouldReturnStatusOK() {
        ResponseModel<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getName()).isEqualTo("OK");
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData()).hasSize(2);
    }

    @Test
    public void testGetAllInbox_ShouldReturnStatusNotFound() {
        when(inboxRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseModel<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getName()).isEqualTo("Not Found");
        assertThat(result.getStatus()).isEqualTo(404);
        assertThat(result.getData()).isNull();
    }

    @Test
    public void testGetAllInbox_ShouldThrowStatusInternalServerError() {
        when(inboxRepository.findAll()).thenThrow(new ExceptionModel());

        ResponseModel<List<InboxModel>> result = inboxService.getAllInbox();

        assertThat(result.getName()).isEqualTo("Internal Server Error");
        assertThat(result.getStatus()).isEqualTo(500);
        assertThat(result.getData()).isNull();
    }

}
