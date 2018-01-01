package pl.lazyteam.pricebreaker.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.lazyteam.pricebreaker.entity.User;
import pl.lazyteam.pricebreaker.event.OnRegistrationCompleteEvent;
import pl.lazyteam.pricebreaker.service.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    @Autowired
    private UserService userService;


    @Autowired
    private JavaMailSender mailSender;

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

        SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    private final SimpleMailMessage constructEmailMessage(OnRegistrationCompleteEvent event, User user, String token)
    {
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registerConfirm.html?token=" + token;
        String message = "You registered successfully. We will send you a confirmation message to your email account.";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + "http://localhost:8080" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
