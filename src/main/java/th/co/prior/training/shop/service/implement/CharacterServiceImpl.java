package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.service.CharacterService;
import th.co.prior.training.shop.units.AccountUtils;
import th.co.prior.training.shop.units.CharacterUtils;
import th.co.prior.training.shop.units.LevelUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final AccountUtils accountUtils;
    private final LevelUtils levelUtils;
    private final CharacterUtils characterUtils;
    private final CharacterRepository characterRepository;

    @Override
    public ResponseModel<List<CharacterModel>> getAllCharacter() {
            ResponseModel<List<CharacterModel>> result = new ResponseModel<>();
            result.setStatus(404);
            result.setMessage("Not Found");

            try {
                List<CharacterEntity> character = this.characterRepository.findAll();

                if(character.iterator().hasNext()) {
                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("Successfully retrieved characters information.");
                    result.setData(this.characterUtils.toDTOList(character));
                } else {
                    result.setDescription("Character not found!");
                }
            } catch (Exception e) {
                result.setStatus(500);
                result.setMessage("Internal Server Error");
                result.setDescription(e.getMessage());
            }

            return result;
    }

    @Override
    public ResponseModel<CharacterModel> getCharacterById(Integer id) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(404);
        result.setMessage("Not Found");

        try {
            CharacterEntity character = characterRepository.findById(id).orElseThrow(() -> new NullPointerException("Character not found!"));

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Successfully retrieved character information.");
            result.setData(this.characterUtils.toDTO(character));
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> createCharacter(String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            Optional<CharacterEntity> duplicateCharacter = this.characterRepository.findCharacterByName(name);

            if(name.length() >= 3) {
                if (duplicateCharacter.isEmpty()) {
                    CharacterEntity character = new CharacterEntity();
                    character.setName(name);
                    character.setLevel(this.levelUtils.getLevel());
                    CharacterEntity saved = this.characterRepository.save(character);
                    this.accountUtils.createAccount(character);

                    result.setStatus(201);
                    result.setMessage("Created");
                    result.setDescription("Successfully created character information.");
                    result.setData(this.characterUtils.toDTO(saved));
                } else {
                    result.setDescription("Duplicate name. Please enter another name.");
                }
            } else {
                result.setDescription("Please enter a name of at least 3 characters.");
            }
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> updateCharacter(Integer id, String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            CharacterEntity character = this.characterRepository.findById(id).orElseThrow(() -> new NullPointerException("Account not found!"));
            Optional<CharacterEntity> duplicateCharacterName = this.characterRepository.findCharacterByName(name);

            if(name.length() >= 3) {
                if (duplicateCharacterName.isEmpty()) {
                    character.setName(name);
                    this.characterRepository.save(character);

                    result.setStatus(200);
                    result.setMessage("OK");
                    result.setDescription("Character information has been successfully updated.");
                    result.setData(this.characterUtils.toDTO(character));
                } else {
                    result.setDescription("Duplicate name. Please enter another name.");
                }
            } else {
                result.setDescription("Please enter a name of at least 3 characters.");
            }
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> deleteCharacter(Integer id) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setMessage("Bad Request");

        try {
            this.characterRepository.findById(id).orElseThrow(() -> new NullPointerException("Character not found!"));
            this.characterRepository.deleteById(id);

            result.setStatus(200);
            result.setMessage("OK");
            result.setDescription("Character information has been deleted.");
            result.setData(null);
        } catch (NullPointerException e){
            result.setDescription(e.getMessage());
        } catch (Exception e) {
            result.setStatus(500);
            result.setMessage("Internal Server Error");
            result.setDescription(e.getMessage());
        }

        return result;
    }
}
