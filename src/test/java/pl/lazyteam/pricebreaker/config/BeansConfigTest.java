package pl.lazyteam.pricebreaker.config;

import junit.framework.TestCase;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class BeansConfigTest extends TestCase {
    BeansConfig beansConfig=new BeansConfig();
    public void testMessageSource() throws Exception {
        assertThat(beansConfig.messageSource(), instanceOf(ResourceBundleMessageSource.class));
    }

}