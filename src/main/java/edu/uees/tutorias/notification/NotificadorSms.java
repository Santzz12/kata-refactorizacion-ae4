package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Notificador SMS simulado para el incremento académico. */
public final class NotificadorSms implements Notificador {
    @Override
    public void enviar(Usuario usuario, String mensaje) {
        System.out.printf("[SMS] %s: %s%n", usuario.getNombre(), mensaje);
    }
}
