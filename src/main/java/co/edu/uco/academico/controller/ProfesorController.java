package co.edu.uco.academico.controller;

import co.edu.uco.academico.dto.Requests.ProfesorRequest;
import co.edu.uco.academico.model.Profesor;
import co.edu.uco.academico.service.ProfesorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@Tag(name = "2. Profesores (administración)", description = "CRUD de profesores")
public class ProfesorController {

    private final ProfesorService service;

    public ProfesorController(ProfesorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar un profesor")
    public Profesor crear(@Valid @RequestBody ProfesorRequest request) {
        return service.crear(request);
    }

    @GetMapping
    @Operation(summary = "Listar profesores")
    public List<Profesor> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un profesor por id")
    public Profesor obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un profesor")
    public Profesor actualizar(@PathVariable Long id, @Valid @RequestBody ProfesorRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un profesor")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
