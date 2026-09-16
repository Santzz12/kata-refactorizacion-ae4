package edu.uees.tutorias.factory;

import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.notification.NotificadorWhatsApp;

public final class WhatsAppFactory extends NotificacionFactory {
    @Override
    public Notificador crearNotificador() {
        return new NotificadorWhatsApp();
    }
}
