package th.co.prior.training.shop.service;

import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.model.ResponseModel;

import java.util.List;

public interface InboxService {
    ResponseModel<List<InboxModel>> getAllInbox();

    ResponseModel<InboxModel> getInboxById(Integer id);

    ResponseModel<InboxModel> createInbox(Integer characterId, String message);

    ResponseModel<InboxModel> updateInbox(Integer id, String message);

    ResponseModel<InboxModel> deleteInbox(Integer id);
}
