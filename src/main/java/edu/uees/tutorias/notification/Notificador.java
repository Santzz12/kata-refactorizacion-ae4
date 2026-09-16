package edu.uees.tutorias.notification;

import edu.uees.tutorias.domain.Usuario;

/** Puerto para comunicar eventos sin acoplar la lógica a una tecnología concreta. */
public interface Notificador {
    void enviar(Usuario usuario, String mensaje);
}
