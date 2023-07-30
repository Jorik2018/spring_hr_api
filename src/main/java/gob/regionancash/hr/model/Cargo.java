package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "cargo")
@Data
public class Cargo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_cargo")
    private Integer idCargo;
    @Size(max = 6)
    @Column(name = "cod_pdt")
    private String codPdt;
    @Size(max = 70)
    @Column(name = "nom_cargo")
    private String nomCargo;
    
    private Integer nivel;

    @Column(name = "orden_firma")
    private Integer ordenFirma;
    @Size(max = 15)
    private String abreviatura;

    @Size(max = 50)
    @Column(name = "abrev")
    private String abrev;

    private Character estado;

}
