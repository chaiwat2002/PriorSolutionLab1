package th.co.prior.training.shop.component.utils.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.component.utils.CharacterUtils;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.repository.CharacterRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CharacterUtilsImpl implements CharacterUtils {

    @Override
    public List<CharacterModel> toDTOList(List<CharacterEntity> character) {
        return character.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterModel toDTO(CharacterEntity character){
        CharacterModel dto = new CharacterModel();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setLevel(character.getLevel().getId());
        dto.setDamage(character.getLevel().getDamage());

        return dto;
    }

}
