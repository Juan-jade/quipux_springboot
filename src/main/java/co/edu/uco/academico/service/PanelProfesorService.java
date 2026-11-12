package co.edu.uco.academico.service;

import co.edu.uco.academico.dto.Requests.AsistenciaRequest;
import co.edu.uco.academico.dto.Requests.NotaRequest;
import co.edu.uco.academico.dto.Requests.PuntoRequest;
import co.edu.uco.academico.dto.Responses.*;
import co.edu.uco.academico.exception.AccesoDenegadoException;
import co.edu.uco.academico.exception.RecursoNoEncontradoException;
import co.edu.uco.academico.exception.ReglaNegocioException;
import co.edu.uco.academico.model.*;
import co.edu.uco.academico.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** Lógica del panel del profesor: solo puede registrar sobre SUS materias y estudiantes matriculados. */
@Service
@Transactional
public class PanelProfesorService {

    private final ProfesorRepository profesorRepo;
    private final MateriaRepository materiaRepo;
    private final EstudianteRepository estudianteRepo;
    private final NotaRepository notaRepo;
    private final AsistenciaRepository asistenciaRepo;
    private final PuntoConductaRepository puntoRepo;

    public PanelProfesorService(ProfesorRepository profesorRepo, MateriaRepository materiaRepo,
                                EstudianteRepository estudianteRepo, NotaRepository notaRepo,
                                AsistenciaRepository asistenciaRepo, PuntoConductaRepository puntoRepo) {
        this.profesorRepo = profesorRepo;
        this.materiaRepo = materiaRepo;
        this.estudianteRepo = estudianteRepo;
        this.notaRepo = notaRepo;
        this.asistenciaRepo = asistenciaRepo;
        this.puntoRepo = puntoRepo;
    }

    // ---------- Consultas ----------

    @Transactional(readOnly = true)
    public PanelProfesorResponse panel(Long profesorId) {
        Profesor p = buscarProfesor(profesorId);
        List<MateriaResponse> materias = materiaRepo.findByProfesorId(profesorId).stream()
                .map(MateriaResponse::from).toList();
        return new PanelProfesorResponse(p.getId(), p.getNombreCompleto(), p.getDepartamento(), materias);
    }

    @Transactional(readOnly = true)
    public List<Estudiante> estudiantes(Long profesorId, Long materiaId) {
        return List.copyOf(materiaDelProfesor(profesorId, materiaId).getEstudiantes());
    }

    @Transactional(readOnly = true)
    public List<NotaResponse> notas(Long profesorId, Long materiaId) {
        materiaDelProfesor(profesorId, materiaId);
        return notaRepo.findByMateriaId(materiaId).stream().map(NotaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> asistencias(Long profesorId, Long materiaId) {
        materiaDelProfesor(profesorId, materiaId);
        return asistenciaRepo.findByMateriaId(materiaId).stream().map(AsistenciaResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<PuntoResponse> puntos(Long profesorId, Long materiaId) {
        materiaDelProfesor(profesorId, materiaId);
        return puntoRepo.findByMateriaId(materiaId).stream().map(PuntoResponse::from).toList();
    }

    // ---------- Registros ----------

    public NotaResponse registrarNota(Long profesorId, NotaRequest r) {
        Materia materia = materiaDelProfesor(profesorId, r.materiaId());
        Estudiante estudiante = estudianteMatriculado(materia, r.estudianteId());

        Nota n = new Nota();
        n.setProfesor(materia.getProfesor());
        n.setMateria(materia);
        n.setEstudiante(estudiante);
        n.setDescripcion(r.descripcion());
        n.setValor(r.valor());
        n.setPorcentaje(r.porcentaje());
        n.setFecha(LocalDateTime.now());
        return NotaResponse.from(notaRepo.save(n));
    }

    public AsistenciaResponse registrarAsistencia(Long profesorId, AsistenciaRequest r) {
        Materia materia = materiaDelProfesor(profesorId, r.materiaId());
        Estudiante estudiante = estudianteMatriculado(materia, r.estudianteId());

        if (r.estado() == EstadoAsistencia.TARDE && (r.minutosTarde() == null || r.minutosTarde() <= 0)) {
            throw new ReglaNegocioException("Para registrar una llegada tarde debes indicar los minutosTarde (> 0)");
        }

        Asistencia a = new Asistencia();
        a.setProfesor(materia.getProfesor());
        a.setMateria(materia);
        a.setEstudiante(estudiante);
        a.setFecha(r.fecha());
        a.setEstado(r.estado());
        a.setMinutosTarde(r.estado() == EstadoAsistencia.TARDE ? r.minutosTarde() : null);
        a.setObservacion(r.observacion());
        return AsistenciaResponse.from(asistenciaRepo.save(a));
    }

    public PuntoResponse registrarPunto(Long profesorId, PuntoRequest r) {
        Materia materia = materiaDelProfesor(profesorId, r.materiaId());
        Estudiante estudiante = estudianteMatriculado(materia, r.estudianteId());

        PuntoConducta p = new PuntoConducta();
        p.setProfesor(materia.getProfesor());
        p.setMateria(materia);
        p.setEstudiante(estudiante);
        p.setTipo(r.tipo());
        p.setPuntos(r.puntos());
        p.setMotivo(r.motivo());
        p.setFecha(LocalDateTime.now());
        return PuntoResponse.from(puntoRepo.save(p));
    }

    // ---------- Validaciones ----------

    private Profesor buscarProfesor(Long id) {
        return profesorRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado: " + id));
    }

    private Materia materiaDelProfesor(Long profesorId, Long materiaId) {
        buscarProfesor(profesorId);
        Materia materia = materiaRepo.findById(materiaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia no encontrada: " + materiaId));
        if (!materia.getProfesor().getId().equals(profesorId)) {
            throw new AccesoDenegadoException("La materia " + materiaId + " no pertenece al profesor " + profesorId);
        }
        return materia;
    }

    private Estudiante estudianteMatriculado(Materia materia, Long estudianteId) {
        Estudiante e = estudianteRepo.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado: " + estudianteId));
        if (!materiaRepo.existsByIdAndEstudiantesId(materia.getId(), estudianteId)) {
            throw new ReglaNegocioException("El estudiante " + estudianteId + " no está matriculado en la materia "
                    + materia.getId());
        }
        return e;
    }
}
