package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.InboxModel;
import th.co.prior.training.shop.repository.InboxRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class InboxUtils {

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

    public Optional<InboxEntity> findInboxById(Integer id){
        return inboxRepository.findById(id);
    }

    public void addInbox(CharacterEntity character, String message) {
            InboxEntity inbox = new InboxEntity();
            inbox.setCharacter(character);
            inbox.setMessage(message);
            this.inboxRepository.save(inbox);
    }
}
