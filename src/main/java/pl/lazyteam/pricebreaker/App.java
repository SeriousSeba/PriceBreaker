package pl.lazyteam.pricebreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication(scanBasePackages = {"pl.lazyteam.pricebreaker"})
public class App
{


    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }

}
