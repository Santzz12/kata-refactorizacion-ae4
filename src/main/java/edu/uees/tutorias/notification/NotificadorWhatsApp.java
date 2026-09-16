package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Notificador WhatsApp simulado para el incremento académico. */
public final class NotificadorWhatsApp implements Notificador {
    @Override
    public void enviar(Usuario usuario, String mensaje) {
        System.out.printf("[WHATSAPP] %s: %s%n", usuario.getNombre(), mensaje);
    }
}
