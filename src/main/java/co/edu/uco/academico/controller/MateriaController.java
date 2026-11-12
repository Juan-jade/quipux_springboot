package co.edu.uco.academico.controller;

import co.edu.uco.academico.dto.Requests.MateriaRequest;
import co.edu.uco.academico.dto.Responses.MateriaResponse;
import co.edu.uco.academico.model.Estudiante;
import co.edu.uco.academico.service.MateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
@Tag(name = "3. Materias y matrículas", description = "Crear materias y matricular estudiantes")
public class MateriaController {

    private final MateriaService service;

    public MateriaController(MateriaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una materia asignada a un profesor")
    public MateriaResponse crear(@Valid @RequestBody MateriaRequest request) {
        return service.crear(request);
    }

    @GetMapping
    @Operation(summary = "Listar materias")
    public List<MateriaResponse> listar() {
        return service.listar();
    }

    @PostMapping("/{materiaId}/estudiantes/{estudianteId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Matricular un estudiante en una materia")
    public MateriaResponse matricular(@PathVariable Long materiaId, @PathVariable Long estudianteId) {
        return service.matricular(materiaId, estudianteId);
    }

    @GetMapping("/{materiaId}/estudiantes")
    @Operation(summary = "Listar los estudiantes matriculados en una materia")
    public List<Estudiante> estudiantes(@PathVariable Long materiaId) {
        return service.estudiantesDeMateria(materiaId);
    }
}
