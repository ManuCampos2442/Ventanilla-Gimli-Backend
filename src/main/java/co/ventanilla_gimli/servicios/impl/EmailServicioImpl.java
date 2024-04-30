package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.EmailDTO;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServicioImpl implements EmailServicio {

    private final JavaMailSender javaMailSender;

   @Override
    public void enviarCorreo(EmailDTO emailDTO) throws Exception {

        MimeMessage mensaje = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje);

        helper.setSubject(emailDTO.asunto());
        helper.setText(emailDTO.mensaje(), true);
        helper.setTo(emailDTO.destinatario());
        helper.setFrom("no_reply@dominio.com");

        javaMailSender.send(mensaje);

    }
}
