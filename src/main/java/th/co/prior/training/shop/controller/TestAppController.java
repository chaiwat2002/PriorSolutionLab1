package th.co.prior.training.shop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prior/api/v1/")
public class TestAppController {

    @GetMapping("/health/check")
    public String home(){
        return "Hello, world!";
    }
}
