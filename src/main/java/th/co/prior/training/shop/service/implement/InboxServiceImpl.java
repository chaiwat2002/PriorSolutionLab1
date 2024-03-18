package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.InboxRepository;
import th.co.prior.training.shop.service.InboxService;
import th.co.prior.training.shop.units.InboxUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {

    private final InboxRepository inboxRepository;
    private final InboxUtils inboxUtils;

    @Override
    public ResponseModel<List<InboxModel>> getAllInbox() {
        ResponseModel<List<InboxModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            List<InboxEntity> inboxes = this.inboxRepository.findAll();

            if(inboxes.iterator().hasNext()) {
                result.setStatus(200);
                result.setMessage("OK");
                result.setDescription("Successfully retrieved inboxes information.");
                result.setData(this.inboxUtils.toDTOList(inboxes));
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e){
            result.setDescription("Inbox not found!");
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<InboxModel> getInboxById(Integer id) {
        ResponseModel<InboxModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            InboxEntity inboxes = this.inboxRepository.findById(id).orElseThrow(() -> new NullPointerException("Inbox not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved inboxes information.");
            result.setData(this.inboxUtils.toDTO(inboxes));
        }catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
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
