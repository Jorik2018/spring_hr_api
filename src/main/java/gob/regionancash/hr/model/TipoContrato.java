package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "tipo_contrato")
@Data
public class TipoContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo_contrato")
    private String tipoContrato;
    
    @Size(max = 60)
    private String nombre;

    @OneToMany(mappedBy = "tipoContrato")
    private Collection<Personal> personalCollection;

}
