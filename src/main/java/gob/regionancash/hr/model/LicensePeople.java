package gob.regionancash.hr.model;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "per_permiso_personal")
@Data
public class LicensePeople implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pl")
    private Integer idPl;
    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "id_espe", referencedColumnName = "id_espe")
    @ManyToOne
    private PerEspecPl perEspecPl;
    @Column(name = "id_dtra")
    private Integer idDtra;

    @Basic(optional = false)
    @NotNull
    private Character tipo;
    
    @Size(max = 100)
    @Column(name = "mension")
    private String mension;
    
    @Column(name = "desde")
    @Temporal(TemporalType.DATE)
    private Date desde;
    
    @Column(name = "hasta")
    @Temporal(TemporalType.DATE)
    private Date hasta;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "recuperable")
    private boolean recuperable;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "recuperado")
    private boolean recuperado;
    
    @Size(max = 60)
    @Column(name = "documento")
    private String documento;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;

}
