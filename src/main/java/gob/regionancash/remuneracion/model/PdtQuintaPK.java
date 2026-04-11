package gob.regionancash.remuneracion.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PdtQuintaPK implements Serializable {

    @NotNull
    @Size(min = 1, max = 8)
    private String dni;

    @NotNull
    private int anio;

    @NotNull
    private int mes;

    public PdtQuintaPK(String dni, int anio, int mes) {
        this.dni = dni;
        this.anio = anio;
        this.mes = mes;
    }
}