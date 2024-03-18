package th.co.prior.training.shop.request;

import lombok.Data;

@Data
public class ItemRequest {

    private Integer characterId;

    private Integer itemId;

    private double price;

}
