package ru.project.denis.FinalProject.services;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.project.denis.FinalProject.models.Content;
import ru.project.denis.FinalProject.models.Film;

import java.io.File;
import java.util.List;

@Service
@PropertySource("classpath:application.properties")
public class MailSender {
    private final JavaMailSender mailSender;
    private final Environment env;

    @Autowired
    public MailSender(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.env = env;
    }

    public void sendMail(String to, List<Film> films) {
        try {
            File file = new File("films.xml");
            JAXBContext context = JAXBContext.newInstance(Content.class);
            Content content = new Content();
            content.setFilmList(films);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(content, file);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            mimeMessage.setFrom(env.getProperty("mail.username"));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject("Ваши сохраненные фильмы!");

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(file);
            mimeBodyPart.setDataHandler(new DataHandler(source));
            mimeBodyPart.setFileName(file.getName());
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);
            mailSender.send(mimeMessage);
        } catch (MessagingException | JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
