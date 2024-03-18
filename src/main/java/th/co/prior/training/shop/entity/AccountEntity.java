package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private double balance;

    @OneToOne
    @JoinColumn(name = "character_id", referencedColumnName = "character_id")
    private CharacterEntity character;

}
