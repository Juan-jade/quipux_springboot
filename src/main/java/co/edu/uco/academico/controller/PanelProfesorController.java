package co.edu.uco.academico.controller;

import co.edu.uco.academico.dto.Requests.AsistenciaRequest;
import co.edu.uco.academico.dto.Requests.NotaRequest;
import co.edu.uco.academico.dto.Requests.PuntoRequest;
import co.edu.uco.academico.dto.Responses.*;
import co.edu.uco.academico.model.Estudiante;
import co.edu.uco.academico.service.PanelProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/panel-profesor/{profesorId}")
@Tag(name = "4. Panel del profesor", description = "Notas, llegadas tarde/asistencia y puntos positivos/negativos")
public class PanelProfesorController {

    private final PanelProfesorService service;

    public PanelProfesorController(PanelProfesorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Resumen del panel: datos del profesor y sus materias")
    public PanelProfesorResponse panel(@PathVariable Long profesorId) {
        return service.panel(profesorId);
    }

    @GetMapping("/materias/{materiaId}/estudiantes")
    @Operation(summary = "Estudiantes matriculados en una materia del profesor")
    public List<Estudiante> estudiantes(@PathVariable Long profesorId, @PathVariable Long materiaId) {
        return service.estudiantes(profesorId, materiaId);
    }

    // ----- Notas -----

    @PostMapping("/notas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar una nota (0.0 a 5.0) a un estudiante")
    public NotaResponse registrarNota(@PathVariable Long profesorId, @Valid @RequestBody NotaRequest request) {
        return service.registrarNota(profesorId, request);
    }

    @GetMapping("/materias/{materiaId}/notas")
    @Operation(summary = "Notas registradas en una materia")
    public List<NotaResponse> notas(@PathVariable Long profesorId, @PathVariable Long materiaId) {
        return service.notas(profesorId, materiaId);
    }

    // ----- Asistencia / llegadas tarde -----

    @PostMapping("/asistencias")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar asistencia (PRESENTE, TARDE, AUSENTE, EXCUSADO). Si es TARDE indicar minutosTarde")
    public AsistenciaResponse registrarAsistencia(@PathVariable Long profesorId,
                                                  @Valid @RequestBody AsistenciaRequest request) {
        return service.registrarAsistencia(profesorId, request);
    }

    @GetMapping("/materias/{materiaId}/asistencias")
    @Operation(summary = "Asistencias y llegadas tarde de una materia")
    public List<AsistenciaResponse> asistencias(@PathVariable Long profesorId, @PathVariable Long materiaId) {
        return service.asistencias(profesorId, materiaId);
    }

    // ----- Puntos de conducta -----

    @PostMapping("/puntos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar puntos POSITIVOS o NEGATIVOS a un estudiante")
    public PuntoResponse registrarPunto(@PathVariable Long profesorId, @Valid @RequestBody PuntoRequest request) {
        return service.registrarPunto(profesorId, request);
    }

    @GetMapping("/materias/{materiaId}/puntos")
    @Operation(summary = "Puntos positivos/negativos registrados en una materia")
    public List<PuntoResponse> puntos(@PathVariable Long profesorId, @PathVariable Long materiaId) {
        return service.puntos(profesorId, materiaId);
    }
}
