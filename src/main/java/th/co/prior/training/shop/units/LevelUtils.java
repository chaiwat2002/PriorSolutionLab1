package th.co.prior.training.shop.units;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.repository.LevelRepository;

@Component
@AllArgsConstructor
public class LevelUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelUtils.class);
    private final LevelRepository levelRepository;

    public LevelEntity getLevel() {
        return this.levelRepository.getReferenceById(1);
    }
}
