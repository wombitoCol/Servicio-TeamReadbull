package com.microservice.Servicio_TeamReadbull.dto.Response;

import com.microservice.Servicio_TeamReadbull.model.JoinRequest.EstadoSolicitud;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JoinRequestResponseDTO {

    private Long id;
    private Long equipoId;
    private Long jugadorId;
    private String mensaje;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
}
