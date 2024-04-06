package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Builder.Default
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber = generateAccountNumber();

    @NonNull
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NonNull
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "character_id", referencedColumnName = "character_id")
    private CharacterEntity character;

    private static String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
