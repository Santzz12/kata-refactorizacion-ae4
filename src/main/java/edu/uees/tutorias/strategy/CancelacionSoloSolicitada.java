package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Reserva;

/** Política restrictiva: solo permite cancelar una reserva aún SOLICITADA. */
public final class CancelacionSoloSolicitada implements PoliticaCancelacion {
    @Override
    public boolean puedeCancelar(Reserva reserva) {
        return reserva.getEstado() == EstadoReserva.SOLICITADA;
    }

    @Override
    public String descripcion() {
        return "Cancelación solo en estado SOLICITADA";
    }
}
