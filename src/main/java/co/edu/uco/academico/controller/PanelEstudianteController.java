package co.edu.uco.academico.controller;

import co.edu.uco.academico.dto.Responses.*;
import co.edu.uco.academico.service.PanelEstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/panel-estudiante/{estudianteId}")
@Tag(name = "5. Panel del estudiante", description = "Consulta de notas, asistencia, llegadas tarde y puntos")
public class PanelEstudianteController {

    private final PanelEstudianteService service;

    public PanelEstudianteController(PanelEstudianteService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Resumen: promedio por materia, tardanzas, ausencias y balance de puntos")
    public PanelEstudianteResponse panel(@PathVariable Long estudianteId) {
        return service.panel(estudianteId);
    }

    @GetMapping("/notas")
    @Operation(summary = "Todas mis notas")
    public List<NotaResponse> notas(@PathVariable Long estudianteId) {
        return service.notas(estudianteId);
    }

    @GetMapping("/asistencias")
    @Operation(summary = "Mi historial de asistencia y llegadas tarde")
    public List<AsistenciaResponse> asistencias(@PathVariable Long estudianteId) {
        return service.asistencias(estudianteId);
    }

    @GetMapping("/puntos")
    @Operation(summary = "Mis puntos positivos y negativos")
    public List<PuntoResponse> puntos(@PathVariable Long estudianteId) {
        return service.puntos(estudianteId);
    }
}
