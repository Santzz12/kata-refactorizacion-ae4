package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Implementación sencilla de notificación para la versión base del sistema. */
public final class NotificadorConsola implements Notificador {
    @Override
    public void enviar(Usuario usuario, String mensaje) {
        System.out.printf("Notificación para %s <%s>: %s%n",
                usuario.getNombre(), usuario.getEmail(), mensaje);
    }
}
