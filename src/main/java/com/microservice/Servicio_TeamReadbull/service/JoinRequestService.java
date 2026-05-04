package com.microservice.Servicio_TeamReadbull.service;

import com.microservice.Servicio_TeamReadbull.dto.Request.JoinRequestRespondDTO;
import com.microservice.Servicio_TeamReadbull.dto.Response.JoinRequestResponseDTO;
import com.microservice.Servicio_TeamReadbull.exception.ForbiddenException;
import com.microservice.Servicio_TeamReadbull.exception.ResourceNotFoundException;
import com.microservice.Servicio_TeamReadbull.model.JoinRequest;
import com.microservice.Servicio_TeamReadbull.model.JoinRequest.EstadoSolicitud;
import com.microservice.Servicio_TeamReadbull.model.Team;
import com.microservice.Servicio_TeamReadbull.repository.JoinRequestRepository;
import com.microservice.Servicio_TeamReadbull.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final TeamRepository teamRepository;

    @Transactional(readOnly = true)
    public List<JoinRequestResponseDTO> listarPendientes(Long equipoId, Long captainId) {
        validarCapitan(equipoId, captainId);
        return joinRequestRepository
                .findByEquipoIdAndEstado(equipoId, EstadoSolicitud.PENDIENTE)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JoinRequestResponseDTO> listarTodas(Long equipoId, Long captainId) {
        validarCapitan(equipoId, captainId);
        return joinRequestRepository.findByEquipoId(equipoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public JoinRequestResponseDTO responderSolicitud(Long equipoId, Long solicitudId,
                                                      Long captainId, JoinRequestRespondDTO dto) {
        validarCapitan(equipoId, captainId);

        JoinRequest solicitud = joinRequestRepository.findById(solicitudId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Solicitud", solicitudId));

        if (!solicitud.getEquipoId().equals(equipoId)) {
            throw new ForbiddenException("La solicitud no pertenece al equipo indicado.");
        }

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ForbiddenException("Esta solicitud ya fue respondida.");
        }

        if (dto.getEstado() == EstadoSolicitud.PENDIENTE) {
            throw new ForbiddenException("Debes responder con ACEPTADO o RECHAZADO.");
        }

        solicitud.setEstado(dto.getEstado());
        solicitud.setFechaRespuesta(LocalDateTime.now());

        if (dto.getEstado() == EstadoSolicitud.ACEPTADO) {
            Team equipo = teamRepository.findById(equipoId)
                    .orElseThrow(() -> ResourceNotFoundException.notFound("Equipo", equipoId));
            equipo.addPlayer(solicitud.getJugadorId());
            teamRepository.save(equipo);
            log.info("Capitán {} aceptó al jugador {} en equipo {}", captainId, solicitud.getJugadorId(), equipoId);
        } else {
            log.info("Capitán {} rechazó al jugador {} del equipo {}", captainId, solicitud.getJugadorId(), equipoId);
        }

        return toResponse(joinRequestRepository.save(solicitud));
    }

    private void validarCapitan(Long equipoId, Long captainId) {
        Team equipo = teamRepository.findById(equipoId)
                .orElseThrow(() -> ResourceNotFoundException.notFound("Equipo", equipoId));
        if (!equipo.getIdCaptain().equals(captainId)) {
            throw new ForbiddenException("Solo el capitán del equipo puede realizar esta acción.");
        }
    }

    private JoinRequestResponseDTO toResponse(JoinRequest s) {
        return JoinRequestResponseDTO.builder()
                .id(s.getId())
                .equipoId(s.getEquipoId())
                .jugadorId(s.getJugadorId())
                .mensaje(s.getMensaje())
                .estado(s.getEstado())
                .fechaSolicitud(s.getFechaSolicitud())
                .fechaRespuesta(s.getFechaRespuesta())
                .build();
    }
}
