package th.co.prior.training.shop.component.utils.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.kafka.utils.InboxKafkaUtils;
import th.co.prior.training.shop.component.utils.InboxUtils;
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
public class InboxUtilsImpl implements InboxUtils {

    @Override
    public List<InboxModel> toDTOList(List<InboxEntity> inbox) {
        return inbox.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public InboxModel toDTO(InboxEntity inbox){
        InboxModel dto = new InboxModel();
        dto.setId(inbox.getId());
        dto.setName(inbox.getCharacter().getName());
        dto.setMessage(inbox.getMessage());

        return dto;
    }

}
