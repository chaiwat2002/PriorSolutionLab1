package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "characters")
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "level_id", referencedColumnName = "level_id")
    private LevelEntity level;

    @JsonIgnore
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
