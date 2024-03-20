package th.co.prior.training.shop.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionModel extends RuntimeException {

    private Integer status = 500;
    private String name = "Internal Server Error";

    public ExceptionModel(String message, Integer status){
        super(message);
        this.status = status;
        this.name = HttpStatus.valueOf(status).getReasonPhrase();
    }
}
