package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Notificador push simulado para el incremento académico. */
public final class NotificadorPush implements Notificador {
    @Override
    public void enviar(Usuario usuario, String mensaje) {
        System.out.printf("[PUSH] %s: %s%n", usuario.getNombre(), mensaje);
    }
}
