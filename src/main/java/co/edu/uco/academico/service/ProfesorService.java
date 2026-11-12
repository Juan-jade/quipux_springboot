package co.edu.uco.academico.service;

import co.edu.uco.academico.dto.Requests.ProfesorRequest;
import co.edu.uco.academico.exception.RecursoNoEncontradoException;
import co.edu.uco.academico.model.Profesor;
import co.edu.uco.academico.repository.ProfesorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfesorService {

    private final ProfesorRepository repo;

    public ProfesorService(ProfesorRepository repo) {
        this.repo = repo;
    }

    public Profesor crear(ProfesorRequest r) {
        return repo.save(aplicar(new Profesor(), r));
    }

    @Transactional(readOnly = true)
    public List<Profesor> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Profesor obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado: " + id));
    }

    public Profesor actualizar(Long id, ProfesorRequest r) {
        return repo.save(aplicar(obtener(id), r));
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

    private Profesor aplicar(Profesor p, ProfesorRequest r) {
        p.setNombre(r.nombre());
        p.setApellido(r.apellido());
        p.setCorreo(r.correo());
        p.setDepartamento(r.departamento());
        return p;
    }
}
