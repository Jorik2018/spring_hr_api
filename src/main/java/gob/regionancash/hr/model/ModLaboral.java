package gob.regionancash.hr.model;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "modalidad_laboral")
@Data
public class ModLaboral implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "mod_laboral")
    private String id;

    @Size(max = 30)
    private String nombre;
    
    @Size(max = 15)
    private String abrev;

    @Size(max = 2)
    @Column(name = "tipo_trab")
    private String tipoTrab;

    @JoinColumn(name = "tipo_trab", referencedColumnName = "tipo_trab", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoTrabajador tipoTrabajador;

}
