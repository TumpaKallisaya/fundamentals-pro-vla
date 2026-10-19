package com.funds.backend.infrastructure.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.funds.backend.application.service.RefrescoService;
import com.funds.backend.application.service.dto.RefrescoDto;

/**
 * Adaptador de entrada: endpoints de refresco.
 */
@RestController
@RequestMapping("/api")
public class RefrescoController {

    private final RefrescoService refrescoService;

    public RefrescoController(RefrescoService refrescoService) {
        this.refrescoService = refrescoService;
    }

    @PostMapping("/refresco")
    public RefrescoDto refrescar() {
        return refrescoService.refrescar();
    }

    @GetMapping("/refresco/ultimo")
    public ResponseEntity<RefrescoDto> ultimo() {
        return refrescoService.ultimo()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
