package co.edu.uco.academico.service;

import co.edu.uco.academico.dto.Requests.MateriaRequest;
import co.edu.uco.academico.dto.Responses.MateriaResponse;
import co.edu.uco.academico.exception.RecursoNoEncontradoException;
import co.edu.uco.academico.exception.ReglaNegocioException;
import co.edu.uco.academico.model.Estudiante;
import co.edu.uco.academico.model.Materia;
import co.edu.uco.academico.repository.EstudianteRepository;
import co.edu.uco.academico.repository.MateriaRepository;
import co.edu.uco.academico.repository.ProfesorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MateriaService {

    private final MateriaRepository materiaRepo;
    private final ProfesorRepository profesorRepo;
    private final EstudianteRepository estudianteRepo;

    public MateriaService(MateriaRepository materiaRepo, ProfesorRepository profesorRepo,
                          EstudianteRepository estudianteRepo) {
        this.materiaRepo = materiaRepo;
        this.profesorRepo = profesorRepo;
        this.estudianteRepo = estudianteRepo;
    }

    public MateriaResponse crear(MateriaRequest r) {
        Materia m = new Materia();
        m.setCodigo(r.codigo());
        m.setNombre(r.nombre());
        m.setProfesor(profesorRepo.findById(r.profesorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado: " + r.profesorId())));
        return MateriaResponse.from(materiaRepo.save(m));
    }

    @Transactional(readOnly = true)
    public List<MateriaResponse> listar() {
        return materiaRepo.findAll().stream().map(MateriaResponse::from).toList();
    }

    public MateriaResponse matricular(Long materiaId, Long estudianteId) {
        Materia materia = buscarMateria(materiaId);
        Estudiante estudiante = estudianteRepo.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado: " + estudianteId));
        if (materiaRepo.existsByIdAndEstudiantesId(materiaId, estudianteId)) {
            throw new ReglaNegocioException("El estudiante ya está matriculado en esta materia");
        }
        materia.getEstudiantes().add(estudiante);
        return MateriaResponse.from(materiaRepo.save(materia));
    }

    @Transactional(readOnly = true)
    public List<Estudiante> estudiantesDeMateria(Long materiaId) {
        return List.copyOf(buscarMateria(materiaId).getEstudiantes());
    }

    private Materia buscarMateria(Long id) {
        return materiaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia no encontrada: " + id));
    }
}
