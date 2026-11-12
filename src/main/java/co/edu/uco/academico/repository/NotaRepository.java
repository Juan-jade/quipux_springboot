package co.edu.uco.academico.repository;

import co.edu.uco.academico.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEstudianteId(Long estudianteId);

    List<Nota> findByMateriaId(Long materiaId);
}
