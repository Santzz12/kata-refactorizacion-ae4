package edu.uees.tutorias;

import edu.uees.tutorias.builder.ReservaBuilder;
import edu.uees.tutorias.domain.Docente;
import edu.uees.tutorias.domain.EstadoReserva;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.factory.EmailFactory;
import edu.uees.tutorias.notification.NotificadorEmail;
import edu.uees.tutorias.repository.MemoriaReservaRepository;
import edu.uees.tutorias.service.ServicioReservas;
import edu.uees.tutorias.strategy.CancelacionEstandar;
import edu.uees.tutorias.strategy.CancelacionSoloSolicitada;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class Ae3PatternsTest {

    @Test
    void builderCreaReservaConValoresPorDefecto() {
        Estudiante estudiante = estudiante();
        HorarioDisponible horario = horario();

        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario)
                .build();

        assertEquals(EstadoReserva.SOLICITADA, reserva.getEstado());
        assertEquals("Sin especificar", reserva.getTema());
        assertEquals("Sin observaciones", reserva.getObservaciones());
        assertFalse(reserva.isEnviarRecordatorio());
    }

    @Test
    void factoryMethodCreaElNotificadorEsperado() {
        assertInstanceOf(NotificadorEmail.class, new EmailFactory().crearNotificador());
    }

    @Test
    void strategyPermiteCambiarLaPoliticaSinModificarElServicio() {
        ServicioReservas servicio = new ServicioReservas(
                new MemoriaReservaRepository(), new CancelacionSoloSolicitada());
        Reserva reserva = servicio.solicitarTutoria(estudiante(), horario());
        servicio.confirmarReserva(reserva.getId());

        assertThrows(IllegalStateException.class,
                () -> servicio.cancelarReserva(reserva.getId()));
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());

        servicio.cambiarPoliticaCancelacion(new CancelacionEstandar());
        servicio.cancelarReserva(reserva.getId());

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertTrue(reserva.getHorario().estaDisponible());
    }

    @Test
    void observerRecibeEventosDeLaReserva() {
        AtomicInteger eventos = new AtomicInteger();
        ServicioReservas servicio = new ServicioReservas(
                new MemoriaReservaRepository(), new CancelacionEstandar());
        servicio.registrarObserver((reserva, evento) -> eventos.incrementAndGet());

        Reserva reserva = servicio.solicitarTutoria(estudiante(), horario());
        servicio.confirmarReserva(reserva.getId());

        assertEquals(2, eventos.get());
    }

    @Test
    void politicaRestrictivaSoloPermiteEstadoSolicitada() {
        CancelacionSoloSolicitada politica = new CancelacionSoloSolicitada();
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante())
                .horario(horario())
                .build();

        assertTrue(politica.puedeCancelar(reserva));
        reserva.confirmar();
        assertFalse(politica.puedeCancelar(reserva));
    }

    private static Estudiante estudiante() {
        return new Estudiante("Ana Torres", "ana.torres@uees.edu.ec", "Computación");
    }

    private static HorarioDisponible horario() {
        Docente docente = new Docente("Carlos Ruiz", "carlos.ruiz@uees.edu.ec", "Programación");
        return docente.publicarHorario(
                LocalDateTime.of(2026, 9, 16, 10, 0),
                LocalDateTime.of(2026, 9, 16, 11, 0));
    }
}
