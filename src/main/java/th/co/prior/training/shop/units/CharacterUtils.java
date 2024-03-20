package th.co.prior.training.shop.units;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
public class CharacterUtils {

    private final CharacterRepository characterRepository;
    private final LevelUtils levelUtils;
    private final AccountUtils accountUtils;

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

    public Optional<CharacterEntity> findCharacterById(Integer id){
        return characterRepository.findById(id);
    }

    public Optional<CharacterEntity> findCharacterByName(String name) { return characterRepository.findCharacterByName(name); }

    @Transactional(rollbackOn = ExceptionModel.class)
    public CharacterEntity createCharacter(String name) {
        CharacterEntity character = new CharacterEntity();
        character.setName(name);
        character.setLevel(this.levelUtils.getLevel());
        CharacterEntity saved = this.characterRepository.save(character);
        this.accountUtils.createAccount(saved);
        return saved;
    }

    public CharacterEntity updateCharacter(CharacterEntity character, String name) {
        this.findCharacterByName(name).orElseThrow(() -> new ExceptionModel("Duplicate name. Please enter another name.", 400));
        character.setName(name);
        return this.characterRepository.save(character);
    }

    public void deleteCharacterById(Integer id) {
        this.characterRepository.deleteById(id);
    }
}
