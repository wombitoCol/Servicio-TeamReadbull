package com.microservice.Servicio_TeamReadbull.dto.Request;

import com.microservice.Servicio_TeamReadbull.model.JoinRequest.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestRespondDTO {

    private EstadoSolicitud estado;
}
