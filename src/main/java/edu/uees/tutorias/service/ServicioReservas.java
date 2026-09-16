package edu.uees.tutorias.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.uees.tutorias.builder.ReservaBuilder;
import edu.uees.tutorias.domain.Estudiante;
import edu.uees.tutorias.domain.HorarioDisponible;
import edu.uees.tutorias.domain.Reserva;
import edu.uees.tutorias.notification.Notificador;
import edu.uees.tutorias.observer.NotificacionReservaObserver;
import edu.uees.tutorias.observer.ReservaObserver;
import edu.uees.tutorias.repository.ReservaRepository;
import edu.uees.tutorias.strategy.CancelacionEstandar;
import edu.uees.tutorias.strategy.PoliticaCancelacion;

public final class ServicioReservas {

    public static final String EVENTO_SOLICITADA = "Nueva tutoría solicitada.";
    public static final String EVENTO_CONFIRMADA = "Tutoría confirmada.";
    public static final String EVENTO_CANCELADA = "Tutoría cancelada.";
    public static final String EVENTO_REPROGRAMADA = "Tutoría reprogramada.";
    public static final String EVENTO_COMPLETADA = "Tutoría completada.";

    private final ReservaRepository reservaRepository;
    private final List<ReservaObserver> observers;
    private PoliticaCancelacion politicaCancelacion;

    public ServicioReservas(ReservaRepository reservaRepository, Notificador notificador) {
        this(reservaRepository, new CancelacionEstandar());
        this.registrarObserver(new NotificacionReservaObserver(notificador));
    }

    public ServicioReservas(ReservaRepository reservaRepository, PoliticaCancelacion politicaCancelacion) {
        this.observers = new ArrayList<>();
        this.reservaRepository = Objects.requireNonNull(reservaRepository, "El repositorio es obligatorio");
        this.politicaCancelacion = Objects.requireNonNull(politicaCancelacion, "La política de cancelación es obligatoria");
    }

    public void registrarObserver(ReservaObserver observer) {
        Objects.requireNonNull(observer, "El observer es obligatorio");
        this.observers.add(observer);
    }

    public void eliminarObserver(ReservaObserver observer) {
        this.observers.remove(observer);
    }

    public void cambiarPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
        this.politicaCancelacion = Objects.requireNonNull(politicaCancelacion, "La política de cancelación es obligatoria");
    }

    public Reserva solicitarTutoria(Estudiante estudiante, HorarioDisponible horario) {
        Objects.requireNonNull(estudiante, "El estudiante es obligatorio");
        Objects.requireNonNull(horario, "El horario es obligatorio");

        if (!horario.estaDisponible()) {
            throw new IllegalStateException("No es posible reservar un horario ocupado");
        }

        horario.reservar();
        Reserva reserva = new ReservaBuilder()
                .estudiante(estudiante)
                .horario(horario)
                .build();

        actualizarYNotificar(reserva, EVENTO_SOLICITADA);
        return reserva;
    }

    public Reserva confirmarReserva(UUID reservaId) {
        Reserva reserva = this.obtenerReserva(reservaId);
        reserva.confirmar();
        actualizarYNotificar(reserva, EVENTO_CONFIRMADA);
        return reserva;
    }

    public Reserva cancelarReserva(UUID reservaId) {
        Reserva reserva = this.obtenerReserva(reservaId);

        if (!this.politicaCancelacion.puedeCancelar(reserva)) {
            throw new IllegalStateException("La reserva no puede cancelarse con la política: " + this.politicaCancelacion.descripcion());
        }

        reserva.cancelar();
        reserva.getHorario().liberar();
        actualizarYNotificar(reserva, EVENTO_CANCELADA);
        return reserva;
    }

    public Reserva reprogramarReserva(UUID reservaId, HorarioDisponible nuevoHorario) {
        Reserva reserva = this.obtenerReserva(reservaId);
        validarReprogramacion(reserva, nuevoHorario);

        HorarioDisponible horarioAnterior = reserva.getHorario();
        nuevoHorario.reservar();

        try {
            reserva.reprogramar(nuevoHorario);
            horarioAnterior.liberar();
            this.reservaRepository.guardar(reserva);
        } catch (RuntimeException ex) {
            nuevoHorario.liberar();
            throw ex;
        }

        this.notificarCambio(reserva, EVENTO_REPROGRAMADA);
        return reserva;
    }

    public Reserva completarReserva(UUID reservaId) {
        Reserva reserva = this.obtenerReserva(reservaId);
        reserva.completar();
        actualizarYNotificar(reserva, EVENTO_COMPLETADA);
        return reserva;
    }

    private void validarReprogramacion(Reserva reserva, HorarioDisponible nuevoHorario) {
        Objects.requireNonNull(nuevoHorario, "El nuevo horario es obligatorio");

        if (!nuevoHorario.estaDisponible()) {
            throw new IllegalStateException("El nuevo horario no está disponible");
        }
        if (!reserva.getDocente().getId().equals(nuevoHorario.getDocente().getId())) {
            throw new IllegalArgumentException("La reprogramación debe conservar al mismo docente");
        }
    }

    private void actualizarYNotificar(Reserva reserva, String evento) {
        this.reservaRepository.guardar(reserva);
        this.notificarCambio(reserva, evento);
    }

    private Reserva obtenerReserva(UUID id) {
        Objects.requireNonNull(id, "El id de reserva es obligatorio");
        return this.reservaRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva con id " + id));
    }

    private void notificarCambio(Reserva reserva, String evento) {
        for (ReservaObserver observer : List.copyOf(this.observers)) {
            observer.actualizar(reserva, evento);
        }
    }
}