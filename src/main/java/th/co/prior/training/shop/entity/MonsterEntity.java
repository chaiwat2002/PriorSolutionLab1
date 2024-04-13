package th.co.prior.training.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "monsters")
public class MonsterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_id")
    private Integer id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull
    @Column(name = "max_health", nullable = false)
    private Integer maxHealth;

    @NonNull
    @Column(name = "drop_item")
    private String dropItem;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "monster", cascade = CascadeType.ALL)
    private Set<InventoryEntity> itemId;
}
