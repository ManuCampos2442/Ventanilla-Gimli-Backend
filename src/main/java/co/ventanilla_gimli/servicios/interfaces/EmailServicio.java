package co.ventanilla_gimli.servicios.interfaces;


import co.ventanilla_gimli.dto.EmailDTO;

public interface EmailServicio {

    void enviarCorreo(EmailDTO emailDTO) throws Exception;


}
