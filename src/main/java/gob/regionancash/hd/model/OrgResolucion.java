package gob.regionancash.hd.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "org_resolucion")
@MenuItem(name="document")
@Data
public class OrgResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private Integer idDtra;

    @Size(max = 40)
    @Column(name = "extension")
    private String extension;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

}
