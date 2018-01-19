package pl.lazyteam.pricebreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"pl.lazyteam.pricebreaker"})
@EnableScheduling
public class App
{


    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }

}
