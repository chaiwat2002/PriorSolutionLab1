package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "inbox")
public class InboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbox_id")
    private Integer id;

    @NonNull
    @Column(name = "message", nullable = false)
    private String message;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "recipient", referencedColumnName = "character_id")
    private CharacterEntity character;

}
