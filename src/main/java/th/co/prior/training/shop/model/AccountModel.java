package th.co.prior.training.shop.model;

import lombok.Data;

@Data
public class AccountModel {

    private Integer id;

    private String customerName;

    private String accountNumber;

    private double balance;

}
