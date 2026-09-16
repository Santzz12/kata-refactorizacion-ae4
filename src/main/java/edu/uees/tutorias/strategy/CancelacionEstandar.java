package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;

/** Política por defecto: permite cancelar reservas que aún no son finales. */
public final class CancelacionEstandar implements PoliticaCancelacion {
    @Override
    public boolean puedeCancelar(Reserva reserva) {
        EstadoReserva estado = reserva.getEstado();
        return estado != EstadoReserva.CANCELADA && estado != EstadoReserva.COMPLETADA;
    }

    @Override
    public String descripcion() {
        return "Cancelación estándar";
    }
}
