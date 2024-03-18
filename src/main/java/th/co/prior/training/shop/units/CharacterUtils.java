package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.repository.CharacterRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CharacterUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterUtils.class);
    private final CharacterRepository characterRepository;

    public List<CharacterModel> toDTOList(List<CharacterEntity> character) {
        return character.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CharacterModel toDTO(CharacterEntity character){
        CharacterModel dto = new CharacterModel();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setLevel(character.getLevel().getId());
        dto.setDamage(character.getLevel().getDamage());

        return dto;
    }

    public List<CharacterEntity> findAllCharacter(){
        return characterRepository.findAll();
    }

    public CharacterEntity findCharacterById(Integer id){
        return characterRepository.findById(id).orElse(null);
    }

    public CharacterEntity findCharacterByName(String name) { return characterRepository.findCharacterByName(name).orElse(null); }
}
