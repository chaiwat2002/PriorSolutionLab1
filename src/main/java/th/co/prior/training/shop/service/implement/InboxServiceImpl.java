package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.service.InboxService;
import th.co.prior.training.shop.component.utils.InboxUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxUtils inboxUtils;

    @Override
    public ResponseModel<List<InboxModel>> getAllInbox() {
        ResponseModel<List<InboxModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Inbox not found!");

        try {
            List<InboxEntity> inboxes = this.inboxUtils.findAllInbox();

            if(inboxes.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved inboxes information.");
                result.setData(this.inboxUtils.toDTOList(inboxes));
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InboxModel> getInboxById(Integer id) {
        ResponseModel<InboxModel> result = new ResponseModel<>();

        try {
            InboxEntity inbox = this.inboxUtils.findInboxById(id)
                    .orElseThrow(() -> new ExceptionModel("Inbox not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved inboxes information.");
            result.setData(this.inboxUtils.toDTO(inbox));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InboxModel> createInbox(Integer characterId, String message) {
        return null;
    }

    @Override
    public ResponseModel<InboxModel> updateInbox(Integer id, String message) {
        return null;
    }

    @Override
    public ResponseModel<InboxModel> deleteInbox(Integer id) {
        return null;
    }
}
