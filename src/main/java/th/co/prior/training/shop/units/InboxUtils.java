package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.repository.InboxRepository;
import th.co.prior.training.shop.service.implement.InboxServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InboxUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboxUtils.class);
    private final EntityUtils entityUtils;
    private final CharacterUtils characterUtils;
    private final InboxRepository inboxRepository;

    public List<InboxModel> toDTOList(List<InboxEntity> inbox) {
        return inbox.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public InboxModel toDTO(InboxEntity inbox){
        InboxModel dto = new InboxModel();
        dto.setId(inbox.getId());
        dto.setName(inbox.getCharacter().getName());
        dto.setMessage(inbox.getMessage());

        return dto;
    }

    public List<InboxEntity> findAllInbox(){
        return inboxRepository.findAll();
    }

    public InboxEntity findInboxById(Integer id){
        return inboxRepository.findById(id).orElse(null);
    }

    public void addInbox(Integer id, String message) {
        try {
            CharacterEntity character = this.characterUtils.findCharacterById(id);

            if(this.entityUtils.hasEntity(character)) {
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
