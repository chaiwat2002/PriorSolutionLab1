package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inbox")
public class InboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbox_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "recipient", referencedColumnName = "character_id")
    private CharacterEntity character;

    @Column(name = "message")
    private String message;

}
