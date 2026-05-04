package com.microservice.Servicio_TeamReadbull.repository;

import com.microservice.Servicio_TeamReadbull.model.JoinRequest;
import com.microservice.Servicio_TeamReadbull.model.JoinRequest.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findByEquipoIdAndEstado(Long equipoId, EstadoSolicitud estado);

    List<JoinRequest> findByEquipoId(Long equipoId);
}
