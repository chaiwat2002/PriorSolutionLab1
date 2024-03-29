package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "market_place")
public class MarketPlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryEntity inventory;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private CharacterEntity character;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "is_sold", columnDefinition = "boolean default false")
    private boolean isSold;

}
