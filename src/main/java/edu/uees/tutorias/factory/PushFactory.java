package edu.uees.tutorias.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorPush;

public final class PushFactory extends NotificacionFactory {
    @Override
    public Notificador crearNotificador() {
        return new NotificadorPush();
    }
}
