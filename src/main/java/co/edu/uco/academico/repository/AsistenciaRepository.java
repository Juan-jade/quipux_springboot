package co.edu.uco.academico.repository;

import co.edu.uco.academico.model.Asistencia;
import co.edu.uco.academico.model.EstadoAsistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByEstudianteId(Long estudianteId);

    List<Asistencia> findByMateriaId(Long materiaId);

    long countByEstudianteIdAndEstado(Long estudianteId, EstadoAsistencia estado);
}
