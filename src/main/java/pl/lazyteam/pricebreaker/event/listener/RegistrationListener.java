package pl.lazyteam.pricebreaker.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.event.OnRegistrationCompleteEvent;
import pl.lazyteam.pricebreaker.service.EmailService;
import pl.lazyteam.pricebreaker.service.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    @Autowired
    private UserService userService;


    @Autowired
    private EmailService emailService;

    @Autowired
    private Environment env;



    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event)
    {

        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
    {
        User user = event.getUser();

        String token = UUID.randomUUID().toString();

        userService.createVerificationTokenForUser(user, token);

        String confirmationUrl = event.getAppUrl() + "/registerConfirm.html?token=" + token;
        String message = "This is the link to activate your account on PriceBreaker:";
        String completeMessage = message + " \r\n" + "http://localhost:8080" + confirmationUrl;
        emailService.send("Registration Confirmation", completeMessage, user.getEmail(), env.getProperty("support.email"));
    }
}
