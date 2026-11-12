package co.edu.uco.academico.repository;

import co.edu.uco.academico.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    List<Materia> findByProfesorId(Long profesorId);

    List<Materia> findByEstudiantesId(Long estudianteId);

    boolean existsByIdAndEstudiantesId(Long materiaId, Long estudianteId);
}
