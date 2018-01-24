package pl.lazyteam.pricebreaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.lazyteam.pricebreaker.dao.LoginDao;
import pl.lazyteam.pricebreaker.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Service that manage email sending.
 */
@Service
public class EmailService
{

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Mathod that sends emails.
     * @param subject email subject
     * @param message email message
     * @param recipient email recipient
     * @param sender email sender
     */
    public void send(String subject, String message, String recipient, String sender)
    {
        SimpleMailMessage email = constructEmailMessage(subject, message, recipient, sender);
        mailSender.send(email);
    }

    /**
     * Method that allows to construct email.
     * @param subject email subject
     * @param message email message
     * @param recipient email recipient
     * @param sender email sender
     * @return
     */
    private SimpleMailMessage constructEmailMessage(String subject, String message, String recipient, String sender)
    {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(sender);
        return email;
    }
}
