package gob.regionancash.remuneracion.model;

import gob.regionancash.remuneracion.model.FuenteFinancPK;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "fuente_financ")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FuenteFinanc {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private FuenteFinancPK fuenteFinancPK;

    @Size(max = 150)
    private String nombre;

    @Size(max = 15)
    private String abrev;

    private Character estado;

    public FuenteFinanc(FuenteFinancPK fuenteFinancPK) {
        this.fuenteFinancPK = fuenteFinancPK;
    }

    public FuenteFinanc(String anoEje, String fuenteFinanc) {
        this.fuenteFinancPK = new FuenteFinancPK(anoEje, fuenteFinanc);
    }

}