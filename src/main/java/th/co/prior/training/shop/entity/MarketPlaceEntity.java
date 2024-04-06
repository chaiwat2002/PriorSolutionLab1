package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "market_place")
public class MarketPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryEntity inventory;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private CharacterEntity character;

    @NonNull
    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "is_sold", columnDefinition = "boolean default false")
    private boolean isSold;

}
