package edu.uees.tutorias.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorSms;

public final class SmsFactory extends NotificacionFactory {
    @Override
    public Notificador crearNotificador() {
        return new NotificadorSms();
    }
}
