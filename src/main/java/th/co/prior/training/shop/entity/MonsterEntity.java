package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "monsters")
public class MonsterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "max_health")
    private Integer maxHealth;

    @Column(name = "drop_item")
    private String dropItem;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "monster", cascade = CascadeType.ALL)
    private Set<InventoryEntity> itemId;
}
