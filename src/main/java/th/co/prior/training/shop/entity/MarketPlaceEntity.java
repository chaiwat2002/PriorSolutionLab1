package th.co.prior.training.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "market_place")
public class MarketPlaceEntity {

    @Id
    @GeneratedValue
    @Column(name = "market_id")
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private InventoryEntity inventory;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private CharacterEntity character;

    @Column(name = "price")
    private double price;

    @Column(name = "is_sold")
    private boolean isSold;

}
