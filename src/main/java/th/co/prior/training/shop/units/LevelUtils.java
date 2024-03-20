package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.repository.LevelRepository;

@Component
@AllArgsConstructor
@Slf4j
public class LevelUtils {

    private final LevelRepository levelRepository;

    public LevelEntity getLevel() {
        return this.levelRepository.getReferenceById(1);
    }
}
