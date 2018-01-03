package pl.lazyteam.pricebreaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class BeansConfig
{
    @Bean
    public ResourceBundleMessageSource messageSource()
    {
        ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
        rb.setBasenames(new String[]{ "messages" });
        return rb;
    }
}
