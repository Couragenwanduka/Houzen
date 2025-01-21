package Houzen.helper;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendOtpMail(String email, String otp) {
        // Sender's email and password (use App Password for Gmail)
        final String senderEmail = "courageobunike@gmail.com";
        final String senderPassword = "iskf qudc deae hekg";

       
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO, 
                    InternetAddress.parse(email)
            );
            message.setSubject("OTP for Email Verification");
            message.setText("Your OTP is: " + otp);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            System.err.println("Error occurred while sending email: " + e.getMessage());
            System.out.println("Failed to send email.");
        }
    }
}
