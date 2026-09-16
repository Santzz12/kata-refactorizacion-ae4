package edu.uees.tutorias.strategy;

import edu.uees.tutorias.domain.Reserva;

/** Política intercambiable para decidir si una reserva puede cancelarse. */
public interface PoliticaCancelacion {
    boolean puedeCancelar(Reserva reserva);

    String descripcion();
}
