package co.edu.uco.academico.service;

import co.edu.uco.academico.dto.Responses.*;
import co.edu.uco.academico.exception.RecursoNoEncontradoException;
import co.edu.uco.academico.model.*;
import co.edu.uco.academico.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Lógica del panel del estudiante (solo lectura de SU información). */
@Service
@Transactional(readOnly = true)
public class PanelEstudianteService {

    private final EstudianteRepository estudianteRepo;
    private final MateriaRepository materiaRepo;
    private final NotaRepository notaRepo;
    private final AsistenciaRepository asistenciaRepo;
    private final PuntoConductaRepository puntoRepo;

    public PanelEstudianteService(EstudianteRepository estudianteRepo, MateriaRepository materiaRepo,
                                  NotaRepository notaRepo, AsistenciaRepository asistenciaRepo,
                                  PuntoConductaRepository puntoRepo) {
        this.estudianteRepo = estudianteRepo;
        this.materiaRepo = materiaRepo;
        this.notaRepo = notaRepo;
        this.asistenciaRepo = asistenciaRepo;
        this.puntoRepo = puntoRepo;
    }

    public PanelEstudianteResponse panel(Long estudianteId) {
        Estudiante e = buscar(estudianteId);

        Map<Long, List<Nota>> notasPorMateria = notaRepo.findByEstudianteId(estudianteId).stream()
                .collect(Collectors.groupingBy(n -> n.getMateria().getId()));

        List<ResumenMateria> materias = materiaRepo.findByEstudiantesId(estudianteId).stream()
                .map(m -> new ResumenMateria(m.getId(), m.getNombre(), m.getProfesor().getNombreCompleto(),
                        calcularPromedio(notasPorMateria.getOrDefault(m.getId(), List.of()))))
                .toList();

        List<PuntoConducta> puntos = puntoRepo.findByEstudianteId(estudianteId);
        int positivos = sumar(puntos, TipoPunto.POSITIVO);
        int negativos = sumar(puntos, TipoPunto.NEGATIVO);

        return new PanelEstudianteResponse(e.getId(), e.getCodigo(), e.getNombreCompleto(), e.getPrograma(),
                materias,
                asistenciaRepo.countByEstudianteIdAndEstado(estudianteId, EstadoAsistencia.TARDE),
                asistenciaRepo.countByEstudianteIdAndEstado(estudianteId, EstadoAsistencia.AUSENTE),
                positivos, negativos, positivos - negativos);
    }

    public List<NotaResponse> notas(Long estudianteId) {
        buscar(estudianteId);
        return notaRepo.findByEstudianteId(estudianteId).stream().map(NotaResponse::from).toList();
    }

    public List<AsistenciaResponse> asistencias(Long estudianteId) {
        buscar(estudianteId);
        return asistenciaRepo.findByEstudianteId(estudianteId).stream().map(AsistenciaResponse::from).toList();
    }

    public List<PuntoResponse> puntos(Long estudianteId) {
        buscar(estudianteId);
        return puntoRepo.findByEstudianteId(estudianteId).stream().map(PuntoResponse::from).toList();
    }

    private Estudiante buscar(Long id) {
        return estudianteRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado: " + id));
    }

    private int sumar(List<PuntoConducta> puntos, TipoPunto tipo) {
        return puntos.stream().filter(p -> p.getTipo() == tipo).mapToInt(PuntoConducta::getPuntos).sum();
    }

    /** Promedio ponderado si todas las notas tienen porcentaje; si no, promedio simple. */
    private Double calcularPromedio(List<Nota> notas) {
        if (notas.isEmpty()) {
            return null;
        }
        double promedio;
        if (notas.stream().allMatch(n -> n.getPorcentaje() != null)) {
            double pesoTotal = notas.stream().mapToDouble(Nota::getPorcentaje).sum();
            promedio = pesoTotal == 0 ? 0
                    : notas.stream().mapToDouble(n -> n.getValor() * n.getPorcentaje()).sum() / pesoTotal;
        } else {
            promedio = notas.stream().mapToDouble(Nota::getValor).average().orElse(0);
        }
        return Math.round(promedio * 100.0) / 100.0;
    }
}
