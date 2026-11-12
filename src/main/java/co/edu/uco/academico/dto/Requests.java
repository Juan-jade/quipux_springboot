package co.edu.uco.academico.dto;

import co.edu.uco.academico.model.EstadoAsistencia;
import co.edu.uco.academico.model.TipoPunto;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/** DTOs de entrada (con validaciones). */
public final class Requests {

    private Requests() {}

    public record EstudianteRequest(
            @NotBlank String codigo,
            @NotBlank String nombre,
            @NotBlank String apellido,
            @NotBlank @Email String correo,
            @NotBlank String programa) {}

    public record ProfesorRequest(
            @NotBlank String nombre,
            @NotBlank String apellido,
            @NotBlank @Email String correo,
            @NotBlank String departamento) {}

    public record MateriaRequest(
            @NotBlank String codigo,
            @NotBlank String nombre,
            @NotNull Long profesorId) {}

    public record NotaRequest(
            @NotNull Long materiaId,
            @NotNull Long estudianteId,
            @NotBlank String descripcion,
            @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double valor,
            @DecimalMin("0.0") @DecimalMax("100.0") Double porcentaje) {}

    public record AsistenciaRequest(
            @NotNull Long materiaId,
            @NotNull Long estudianteId,
            @NotNull LocalDate fecha,
            @NotNull EstadoAsistencia estado,
            @Min(0) Integer minutosTarde,
            String observacion) {}

    public record PuntoRequest(
            @NotNull Long materiaId,
            @NotNull Long estudianteId,
            @NotNull TipoPunto tipo,
            @NotNull @Min(1) Integer puntos,
            @NotBlank String motivo) {}
}
