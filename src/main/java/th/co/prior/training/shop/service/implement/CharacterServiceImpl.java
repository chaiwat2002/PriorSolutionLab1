package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.modal.ResponseModal;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.service.CharacterService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    @Override
    public ResponseModal<List<CharacterEntity>> getAllCharacter() {
            ResponseModal<List<CharacterEntity>> result = new ResponseModal<>();
            result.setStatus(404);
            result.setDescription("Not Found!");

            try {
                List<CharacterEntity> characters = this.characterRepository.findAll();

                if(characters.iterator().hasNext()) {
                    result.setStatus(200);
                    result.setDescription("OK");
                    result.setData(characters);
                }
            } catch (Exception e) {
                result.setStatus(500);
                result.setDescription(e.getMessage());
            }

            return result;
    }

    @Override
    public ResponseModal<CharacterEntity> getCharacterById(Integer id) {
        ResponseModal<CharacterEntity> result = new ResponseModal<>();
        result.setStatus(404);
        result.setDescription("Not Found!");

        try {
            CharacterEntity character = characterRepository.findById(id).orElseThrow(() -> new RuntimeException("Character not found!"));

            result.setStatus(200);
            result.setDescription("OK");
            result.setData(character);
        } catch (Exception e) {
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
