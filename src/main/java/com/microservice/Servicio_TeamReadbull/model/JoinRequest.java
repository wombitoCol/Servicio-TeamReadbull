package com.microservice.Servicio_TeamReadbull.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(
    name = "solicitudes_vinculacion",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"equipo_id", "jugador_id"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipo_id", nullable = false)
    private Long equipoId;

    @Column(name = "jugador_id", nullable = false)
    private Long jugadorId;

    @Column(length = 500)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    public enum EstadoSolicitud {
        PENDIENTE,
        ACEPTADO,
        RECHAZADO
    }
}
