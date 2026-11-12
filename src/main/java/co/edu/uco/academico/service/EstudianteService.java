package co.edu.uco.academico.service;

import co.edu.uco.academico.dto.Requests.EstudianteRequest;
import co.edu.uco.academico.exception.RecursoNoEncontradoException;
import co.edu.uco.academico.model.Estudiante;
import co.edu.uco.academico.repository.EstudianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstudianteService {

    private final EstudianteRepository repo;

    public EstudianteService(EstudianteRepository repo) {
        this.repo = repo;
    }

    public Estudiante crear(EstudianteRequest r) {
        return repo.save(aplicar(new Estudiante(), r));
    }

    @Transactional(readOnly = true)
    public List<Estudiante> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Estudiante obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado: " + id));
    }

    public Estudiante actualizar(Long id, EstudianteRequest r) {
        return repo.save(aplicar(obtener(id), r));
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

    private Estudiante aplicar(Estudiante e, EstudianteRequest r) {
        e.setCodigo(r.codigo());
        e.setNombre(r.nombre());
        e.setApellido(r.apellido());
        e.setCorreo(r.correo());
        e.setPrograma(r.programa());
        return e;
    }
}
