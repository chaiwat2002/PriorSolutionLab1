package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "on_market", columnDefinition = "boolean default false")
    private boolean onMarket;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory", cascade = CascadeType.ALL)
    private Set<MarketPlaceEntity> marketPlace;

    @ManyToOne
    @JoinColumn(name = "character_id", referencedColumnName = "character_id", nullable = false)
    private CharacterEntity character;

    @ManyToOne
    @JoinColumn(name = "monster_id", referencedColumnName = "monster_id", nullable = false)
    private MonsterEntity monster;
}