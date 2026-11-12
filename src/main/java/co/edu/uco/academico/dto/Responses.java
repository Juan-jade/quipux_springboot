package co.edu.uco.academico.dto;

import co.edu.uco.academico.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/** DTOs de salida. Los métodos from(...) se usan dentro de la transacción del servicio. */
public final class Responses {

    private Responses() {}

    public record MateriaResponse(Long id, String codigo, String nombre, Long profesorId, String profesor,
                                  int totalEstudiantes) {
        public static MateriaResponse from(Materia m) {
            return new MateriaResponse(m.getId(), m.getCodigo(), m.getNombre(), m.getProfesor().getId(),
                    m.getProfesor().getNombreCompleto(), m.getEstudiantes().size());
        }
    }

    public record NotaResponse(Long id, Long estudianteId, String estudiante, Long materiaId, String materia,
                               String descripcion, Double valor, Double porcentaje, LocalDateTime fecha) {
        public static NotaResponse from(Nota n) {
            return new NotaResponse(n.getId(), n.getEstudiante().getId(), n.getEstudiante().getNombreCompleto(),
                    n.getMateria().getId(), n.getMateria().getNombre(), n.getDescripcion(), n.getValor(),
                    n.getPorcentaje(), n.getFecha());
        }
    }

    public record AsistenciaResponse(Long id, Long estudianteId, String estudiante, Long materiaId, String materia,
                                     LocalDate fecha, EstadoAsistencia estado, Integer minutosTarde,
                                     String observacion) {
        public static AsistenciaResponse from(Asistencia a) {
            return new AsistenciaResponse(a.getId(), a.getEstudiante().getId(), a.getEstudiante().getNombreCompleto(),
                    a.getMateria().getId(), a.getMateria().getNombre(), a.getFecha(), a.getEstado(),
                    a.getMinutosTarde(), a.getObservacion());
        }
    }

    public record PuntoResponse(Long id, Long estudianteId, String estudiante, Long materiaId, String materia,
                                String profesor, TipoPunto tipo, Integer puntos, String motivo,
                                LocalDateTime fecha) {
        public static PuntoResponse from(PuntoConducta p) {
            return new PuntoResponse(p.getId(), p.getEstudiante().getId(), p.getEstudiante().getNombreCompleto(),
                    p.getMateria().getId(), p.getMateria().getNombre(), p.getProfesor().getNombreCompleto(),
                    p.getTipo(), p.getPuntos(), p.getMotivo(), p.getFecha());
        }
    }

    // ----- Paneles -----

    public record PanelProfesorResponse(Long id, String nombre, String departamento, List<MateriaResponse> materias) {}

    public record ResumenMateria(Long materiaId, String materia, String profesor, Double promedio) {}

    public record PanelEstudianteResponse(Long id, String codigo, String nombre, String programa,
                                          List<ResumenMateria> materias, long totalTardanzas, long totalAusencias,
                                          int puntosPositivos, int puntosNegativos, int balancePuntos) {}
}
