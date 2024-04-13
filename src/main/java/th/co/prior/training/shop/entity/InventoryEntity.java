package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;

    @NonNull
    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "on_market", columnDefinition = "boolean default false")
    private boolean onMarket;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory", cascade = CascadeType.ALL)
    private Set<MarketPlaceEntity> marketPlace;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "character_id", referencedColumnName = "character_id", nullable = false)
    private CharacterEntity character;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "monster_id", referencedColumnName = "monster_id", nullable = false)
    private MonsterEntity monster;
<<<<<<< HEAD
}
=======

}
>>>>>>> develop
