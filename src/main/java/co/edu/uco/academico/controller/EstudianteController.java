package co.edu.uco.academico.controller;

import co.edu.uco.academico.dto.Requests.EstudianteRequest;
import co.edu.uco.academico.model.Estudiante;
import co.edu.uco.academico.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@Tag(name = "1. Estudiantes (administración)", description = "CRUD de estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un estudiante")
    public Estudiante crear(@Valid @RequestBody EstudianteRequest request) {
        return service.crear(request);
    }

    @GetMapping
    @Operation(summary = "Listar estudiantes")
    public List<Estudiante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un estudiante por id")
    public Estudiante obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estudiante")
    public Estudiante actualizar(@PathVariable Long id, @Valid @RequestBody EstudianteRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un estudiante")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
