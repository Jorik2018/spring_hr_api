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
@Table(name = "nivel_educativo")
@Data
public class NivelEducativo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "nivel_educativo")
    private String id;
    
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    
    @OneToMany(mappedBy = "nivelEducativo")
    private Collection<Personal> personalCollection;
    
}
