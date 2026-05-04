package com.microservice.Servicio_TeamReadbull.controllers;

import com.microservice.Servicio_TeamReadbull.dto.Request.JoinRequestRespondDTO;
import com.microservice.Servicio_TeamReadbull.dto.Response.JoinRequestResponseDTO;
import com.microservice.Servicio_TeamReadbull.service.JoinRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/equipos/{id}/solicitudes")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<JoinRequestResponseDTO>> listarPendientes(
            @PathVariable Long id,
            @RequestHeader("X-Captain-Id") Long captainId) {
        return ResponseEntity.ok(joinRequestService.listarPendientes(id, captainId));
    }

    @GetMapping
    public ResponseEntity<List<JoinRequestResponseDTO>> listarTodas(
            @PathVariable Long id,
            @RequestHeader("X-Captain-Id") Long captainId) {
        return ResponseEntity.ok(joinRequestService.listarTodas(id, captainId));
    }

    @PatchMapping("/{solicitudId}")
    public ResponseEntity<JoinRequestResponseDTO> responderSolicitud(
            @PathVariable Long id,
            @PathVariable Long solicitudId,
            @RequestHeader("X-Captain-Id") Long captainId,
            @RequestBody JoinRequestRespondDTO dto) {
        return ResponseEntity.ok(
                joinRequestService.responderSolicitud(id, solicitudId, captainId, dto));
    }
}
