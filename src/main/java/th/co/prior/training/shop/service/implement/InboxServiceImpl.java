package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.InboxRepository;
import th.co.prior.training.shop.service.InboxService;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class InboxServiceImpl implements InboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboxServiceImpl.class);
    private final InboxRepository inboxRepository;
    private final CharacterServiceImpl characterService;

    @Override
    public ResponseModal<List<InboxEntity>> getAllInbox() {
        ResponseModal<List<InboxEntity>> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            List<InboxEntity> inboxes = this.inboxRepository.findAll();

            if(inboxes.iterator().hasNext()) {
                result.setStatus(200);
                result.setDescription("OK");
                result.setData(inboxes);
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public void addInbox(Integer id, String message) {
        try {
            CharacterEntity character = characterService.getCharacterById(id).getData();

            if(Objects.nonNull(character)) {
                InboxEntity inbox = new InboxEntity();
                inbox.setCharacter(character);
                inbox.setMessage(message);
                this.inboxRepository.save(inbox);
            }
        } catch (Exception e) {
            LOGGER.error("error: {}", e.getMessage());
        }
    }
}
