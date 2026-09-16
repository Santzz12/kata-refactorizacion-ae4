package edu.uees.tutorias.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorEmail;

public final class EmailFactory extends NotificacionFactory {
    @Override
    public Notificador crearNotificador() {
        return new NotificadorEmail();
    }
}
