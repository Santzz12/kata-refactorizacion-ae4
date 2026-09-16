package edu.uees.tutorias.observer;

import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;

import java.util.Objects;

/** Observer que comunica a estudiante y docente cada cambio relevante. */
public final class NotificacionReservaObserver implements ReservaObserver {
    private final Notificador notificador;

    public NotificacionReservaObserver(Notificador notificador) {
        this.notificador = Objects.requireNonNull(notificador, "El notificador es obligatorio");
    }

    @Override
    public void actualizar(Reserva reserva, String evento) {
        String mensaje = evento + " Estado: " + reserva.getEstado();
        notificador.enviar(reserva.getEstudiante(), mensaje);
        notificador.enviar(reserva.getDocente(), mensaje);
    }
}
