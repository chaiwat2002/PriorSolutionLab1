package th.co.prior.training.shop.model;

import lombok.Data;

@Data
public class MarketPlaceModel {

    private Integer id;

    private String item;

    private String seller;

    private double price;

    private boolean soldStatus;

}
