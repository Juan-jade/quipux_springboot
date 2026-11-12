package co.edu.uco.academico.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notas")
@Getter @Setter @NoArgsConstructor
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id")
    private Materia materia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    @Column(nullable = false, length = 150)
    private String descripcion;

    /** Nota de 0.0 a 5.0 */
    @Column(nullable = false)
    private Double valor;

    /** Peso porcentual opcional (0-100) */
    private Double porcentaje;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
