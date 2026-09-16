package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Notificador por correo simulado para el incremento académico. */
public final class NotificadorEmail implements Notificador {
    @Override
    public void enviar(Usuario usuario, String mensaje) {
        System.out.printf("[EMAIL] %s <%s>: %s%n", usuario.getNombre(), usuario.getEmail(), mensaje);
    }
}
