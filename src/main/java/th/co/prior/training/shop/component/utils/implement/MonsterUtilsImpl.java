package th.co.prior.training.shop.component.utils.implement;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.utils.MonsterUtils;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.model.MonsterModel;
import th.co.prior.training.shop.repository.MonsterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class MonsterUtilsImpl implements MonsterUtils {

    @Override
    public List<MonsterModel> toDTOList(List<MonsterEntity> monster) {
        return monster.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MonsterModel toDTO(MonsterEntity monster) {
        MonsterModel dto = new MonsterModel();
        dto.setId(monster.getId());
        dto.setName(monster.getName());
        dto.setMaxHealth(monster.getMaxHealth());
        dto.setDropItem(monster.getDropItem());

        return dto;
    }

}
