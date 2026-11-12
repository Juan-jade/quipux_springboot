package co.edu.uco.academico.repository;

import co.edu.uco.academico.model.PuntoConducta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuntoConductaRepository extends JpaRepository<PuntoConducta, Long> {
    List<PuntoConducta> findByEstudianteId(Long estudianteId);

    List<PuntoConducta> findByMateriaId(Long materiaId);
}
