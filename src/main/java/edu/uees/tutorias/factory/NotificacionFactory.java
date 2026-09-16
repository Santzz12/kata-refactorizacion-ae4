package edu.uees.tutorias.factory;

import edu.uees.tutorias.domain.Usuario;
import edu.uees.tutorias.notification.Notificador;

/** Creator de Factory Method para los canales de notificación. */
public abstract class NotificacionFactory {
    public abstract Notificador crearNotificador();

    public void notificar(Usuario usuario, String mensaje) {
        crearNotificador().enviar(usuario, mensaje);
    }
}
