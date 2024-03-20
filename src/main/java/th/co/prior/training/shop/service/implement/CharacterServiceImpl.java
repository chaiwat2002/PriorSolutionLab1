package th.co.prior.training.shop.service.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
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

    private final CharacterUtils characterUtils;

    @Override
    public ResponseModel<List<CharacterModel>> getAllCharacter() {
            ResponseModel<List<CharacterModel>> result = new ResponseModel<>();
            result.setStatus(404);
            result.setName("Not Found");
            result.setMessage("Character not found!");

            try {
                List<CharacterEntity> character = this.characterUtils.findAllCharacter();

                if(character.iterator().hasNext()) {
                    result.setStatus(200);
                    result.setName("OK");
                    result.setMessage("Successfully retrieved characters information.");
                    result.setData(this.characterUtils.toDTOList(character));
                }
            } catch (ExceptionModel e) {
                result.setStatus(e.getStatus());
                result.setName(e.getName());
                result.setMessage(e.getMessage());
            }

            return result;
    }

    @Override
    public ResponseModel<CharacterModel> getCharacterById(Integer id) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(id)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved character information.");
            result.setData(this.characterUtils.toDTO(character));
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> createCharacter(String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");

        try {
            if(name.length() >= 3) {
                CharacterEntity saved = this.characterUtils.createCharacter(name);

                result.setStatus(201);
                result.setName("Created");
                result.setMessage("Successfully created character information.");
                result.setData(this.characterUtils.toDTO(saved));
            } else {
                result.setMessage("Please enter a name of at least 3 characters.");
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> updateCharacter(Integer id, String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();
        result.setStatus(400);
        result.setName("Bad Request");

        try {
            CharacterEntity character = this.characterUtils.findCharacterById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));

            if(name.length() >= 3) {
                CharacterEntity saved = this.characterUtils.updateCharacter(character, name);

                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Character information has been successfully updated.");
                result.setData(this.characterUtils.toDTO(saved));
            } else {
                result.setMessage("Please enter a name of at least 3 characters.");
            }
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> deleteCharacter(Integer id) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();

        try {
            this.characterUtils.findCharacterById(id)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            this.characterUtils.deleteCharacterById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Character information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
