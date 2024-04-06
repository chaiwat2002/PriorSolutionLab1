package th.co.prior.training.shop.service.implement;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.model.CharacterModel;
import th.co.prior.training.shop.model.ExceptionModel;
import th.co.prior.training.shop.model.ResponseModel;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.LevelRepository;
import th.co.prior.training.shop.service.AccountService;
import th.co.prior.training.shop.service.CharacterService;
import th.co.prior.training.shop.component.utils.CharacterUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final AccountRepository accountRepository;
    private final LevelRepository levelRepository;
    private final CharacterUtils characterUtils;

    @Override
    public ResponseModel<List<CharacterModel>> getAllCharacter() {
        ResponseModel<List<CharacterModel>> result = new ResponseModel<>();
        result.setStatus(404);
        result.setName("Not Found");
        result.setMessage("Character not found!");

        try {
            List<CharacterEntity> character = this.characterRepository.findAll();

            if (character.iterator().hasNext()) {
                result.setStatus(200);
                result.setName("OK");
                result.setMessage("Successfully retrieved characters information.");
                result.setData(this.characterUtils.toDTOList(character));
            }
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching character", e);
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
            CharacterEntity character = this.characterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Successfully retrieved character information.");
            result.setData(this.characterUtils.toDTO(character));
        } catch (ExceptionModel e) {
            log.error("Error occurred while searching character", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional(rollbackOn = ExceptionModel.class)
    public ResponseModel<CharacterModel> createCharacter(String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();

        try {
            this.characterRepository.findCharacterByName(name)
                    .ifPresent(e -> {
                        throw new ExceptionModel("Duplicate name. Please enter another name.", 400);
                    });
            LevelEntity level = this.levelRepository.findById(1)
                    .orElseThrow(() -> new ExceptionModel("System error, Please try again!"));

            if (name.length() < 3) {
                throw new ExceptionModel("Name must be at least 3 characters.", 400);
            }

            CharacterEntity saved = this.characterRepository.save(new CharacterEntity(name, level));
            this.accountRepository.save(new AccountEntity(3000.00, saved));

            result.setStatus(201);
            result.setName("Created");
            result.setMessage("Successfully created character information.");
            result.setData(this.characterUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            log.error("Error occurred while creating character", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @Override
    public ResponseModel<CharacterModel> updateCharacter(Integer id, String name) {
        ResponseModel<CharacterModel> result = new ResponseModel<>();

        try {
            CharacterEntity character = this.characterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Account not found!", 404));
            this.characterRepository.findCharacterByName(name)
                    .ifPresent(e -> {
                        throw new ExceptionModel("Duplicate name. Please enter another name.", 400);
                    });

            if (name.length() < 3) {
                throw new ExceptionModel("Name must be at least 3 characters.", 400);
            }

            character.setName(name);
            CharacterEntity saved = this.characterRepository.save(character);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Character information has been successfully updated.");
            result.setData(this.characterUtils.toDTO(saved));
        } catch (ExceptionModel e) {
            log.error("Error occurred while updating character", e);
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
            this.characterRepository.findById(id)
                    .orElseThrow(() -> new ExceptionModel("Character not found!", 404));
            this.characterRepository.deleteById(id);

            result.setStatus(200);
            result.setName("OK");
            result.setMessage("Character information has been deleted.");
            result.setData(null);
        } catch (ExceptionModel e) {
            log.error("Error occurred while deleting character", e);
            result.setStatus(e.getStatus());
            result.setName(e.getName());
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
