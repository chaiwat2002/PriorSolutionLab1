package th.co.prior.training.shop.component.utils;

import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.InboxEntity;
import th.co.prior.training.shop.model.InboxModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface InboxUtils {

    List<InboxModel> toDTOList(List<InboxEntity> inbox);

    InboxModel toDTO(InboxEntity inbox);

}
