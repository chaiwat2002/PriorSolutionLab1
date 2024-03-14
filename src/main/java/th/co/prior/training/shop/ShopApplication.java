package th.co.prior.training.shop;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import th.co.prior.training.shop.entity.AccountEntity;
import th.co.prior.training.shop.entity.CharacterEntity;
import th.co.prior.training.shop.entity.MonsterEntity;
import th.co.prior.training.shop.repository.AccountRepository;
import th.co.prior.training.shop.repository.CharacterRepository;
import th.co.prior.training.shop.repository.MonsterRepository;

@SpringBootApplication
@AllArgsConstructor
public class ShopApplication {

    private final CharacterRepository characterRepository;
    private final MonsterRepository monsterRepository;
    private final AccountRepository accountRepository;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            CharacterEntity character1 = new CharacterEntity();
            character1.setId(1);
            character1.setName("cwpd");

            CharacterEntity character2 = new CharacterEntity();
            character2.setId(2);
            character2.setName("ion");

            characterRepository.save(character1);
            characterRepository.save(character2);

            MonsterEntity monster1 = new MonsterEntity();
            monster1.setId(1);
            monster1.setName("Pikachu");
            monster1.setHealth(50000);
            monster1.setDropItem("Sword");

            MonsterEntity monster2 = new MonsterEntity();
            monster2.setId(2);
            monster2.setName("Doraemon");
            monster2.setHealth(43000);
            monster2.setDropItem("Knife");

            monsterRepository.save(monster1);
            monsterRepository.save(monster2);

            AccountEntity account1 = new AccountEntity();
            account1.setId(1);
            account1.setAccountNumber("0949");
            account1.setBalance(30000.41);
            account1.setCharacters(character1);

            AccountEntity account2 = new AccountEntity();
            account2.setId(2);
            account2.setAccountNumber("0309");
            account2.setBalance(24000.85);
            account2.setCharacters(character2);

            accountRepository.save(account1);
            accountRepository.save(account2);
        };
    }

}
