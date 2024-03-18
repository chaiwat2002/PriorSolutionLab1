package th.co.prior.training.shop;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.LevelEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.LevelRepository;
import th.co.prior.training.shop.repository.MonsterRepository;
import th.co.prior.training.shop.units.AccountUtils;

@SpringBootApplication
@AllArgsConstructor
public class ShopApplication {

    private final CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;
    private final AccountRepository accountRepository;
    private final AccountUtils accountUtils;
    private final LevelRepository levelRepository;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            for (int i = 1; i <= 100; i++) {
                LevelEntity level = new LevelEntity();
                level.setId(i);
                level.setDamage(500 * i);

                levelRepository.save(level);
            }

            CharacterEntity character1 = new CharacterEntity();
            character1.setId(1);
            character1.setName("cwpd");
            character1.setLevel(levelRepository.findById(17).get());

            CharacterEntity character2 = new CharacterEntity();
            character2.setId(2);
            character2.setName("ion");
            character2.setLevel(levelRepository.findById(63).get());

            CharacterEntity character3 = new CharacterEntity();
            character3.setId(3);
            character3.setName("drm");
            character3.setLevel(levelRepository.findById(100).get());

            characterRepository.save(character1);
            characterRepository.save(character2);
            characterRepository.save(character3);

            MonsterEntity monster1 = new MonsterEntity();
            monster1.setId(1);
            monster1.setName("Pikachu");
            monster1.setMaxHealth(50000);
            monster1.setDropItem("Sword");

            MonsterEntity monster2 = new MonsterEntity();
            monster2.setId(2);
            monster2.setName("Doraemon");
            monster2.setMaxHealth(43000);
            monster2.setDropItem("Knife");

            monsterRepository.save(monster1);
            monsterRepository.save(monster2);

            AccountEntity account1 = new AccountEntity();
            account1.setId(1);
            account1.setAccountNumber(this.accountUtils.getAccountNumber());
            account1.setBalance(30000.41);
            account1.setCharacter(character1);

            AccountEntity account2 = new AccountEntity();
            account2.setId(2);
            account2.setAccountNumber(this.accountUtils.getAccountNumber());
            account2.setBalance(24000.85);
            account2.setCharacter(character2);

            AccountEntity account3 = new AccountEntity();
            account3.setId(3);
            account3.setAccountNumber(this.accountUtils.getAccountNumber());
            account3.setBalance(50000.85);
            account3.setCharacter(character3);

            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
        };
    }

}
