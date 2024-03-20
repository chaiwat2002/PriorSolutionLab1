package th.co.prior.training.shop.model;

import lombok.Data;

@Data
public class ResponseModel<T> {

    private Integer status;
    private String name;
    private String message;
    private T data;

}
