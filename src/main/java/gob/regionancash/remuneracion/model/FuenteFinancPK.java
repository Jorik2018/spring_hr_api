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
public class FuenteFinancPK implements Serializable {

    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "ano_eje")
    private String anoEje;

    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "fuente_financ")
    private String fuenteFinanc;

    public FuenteFinancPK(String anoEje, String fuenteFinanc) {
        this.anoEje = anoEje;
        this.fuenteFinanc = fuenteFinanc;
    }
}