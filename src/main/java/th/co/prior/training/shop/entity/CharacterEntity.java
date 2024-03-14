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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "character_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "character")
    private Set<InventoryEntity> inventory;

    @JsonIgnore
    @OneToMany(mappedBy = "character")
    private Set<MarketPlaceEntity> marketPlace;

    @JsonIgnore
    @OneToMany(mappedBy = "character")
    private Set<InboxEntity> inbox;
}
