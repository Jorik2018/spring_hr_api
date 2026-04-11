package gob.regionancash.remuneracion.model;

import gob.regionancash.remuneracion.model.PdtQuintaPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "pdt_quinta")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PdtQuinta {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private PdtQuintaPK pdtQuintaPK;

    private BigDecimal bruto;

    private BigDecimal quinta;

    public PdtQuinta(PdtQuintaPK pdtQuintaPK) {
        this.pdtQuintaPK = pdtQuintaPK;
    }

    public PdtQuinta(String dni, int anio, int mes) {
        this.pdtQuintaPK = new PdtQuintaPK(dni, anio, mes);
    }
}