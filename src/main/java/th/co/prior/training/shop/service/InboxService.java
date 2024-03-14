package th.co.prior.training.shop.service;

import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.modal.ResponseModal;

import java.util.List;

public interface InboxService {
    ResponseModal<List<InboxEntity>> getAllInbox();

    void addInbox(Integer id, String message);

}
