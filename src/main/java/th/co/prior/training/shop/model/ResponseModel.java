package th.co.prior.training.shop.model;

import lombok.Data;

@Data
public class ResponseModel<T> {

    private Integer status;
    private String message;
    private String description;
    private T data;

}
