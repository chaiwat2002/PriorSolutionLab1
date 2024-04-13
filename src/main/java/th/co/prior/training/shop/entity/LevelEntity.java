package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "level")
public class LevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Integer id;

    @NonNull
    @Column(name = "damage", nullable = false)
    private Integer damage;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CharacterEntity> character;
}
