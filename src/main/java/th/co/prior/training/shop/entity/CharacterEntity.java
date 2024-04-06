package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "characters")
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Integer id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "level_id", nullable = false)
    private LevelEntity level;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "character", cascade = CascadeType.ALL)
    private AccountEntity account;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "character", cascade = CascadeType.ALL)
    private Set<InventoryEntity> inventory;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "character", cascade = CascadeType.ALL)
    private Set<MarketPlaceEntity> marketPlace;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "character", cascade = CascadeType.ALL)
    private Set<InboxEntity> inbox;

}
