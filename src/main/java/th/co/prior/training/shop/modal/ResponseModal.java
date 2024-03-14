package th.co.prior.training.shop.modal;

import lombok.Data;

@Data
public class ResponseModal<T> {

    private Integer status;
    private String description;
    private T data;

}
