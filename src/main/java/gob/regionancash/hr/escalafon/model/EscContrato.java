package gob.regionancash.hr.escalafon.model;

import gob.regionancash.hr.model.Employee;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "esc_contratos")
@Data
public class EscContrato implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @Column(name = "id_dtra")
    private String idDtra;
    
    @Column(name = "mencion")
    private String mencion;
    
    @Basic(optional = false)
    @Column(name = "fecha_ini")
    @Temporal(TemporalType.DATE)
    private Date fechaIni;
    
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_cn")
    private Integer idCn;
    
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    
    @Basic(optional = false)
    private boolean jefe;
    
    private Boolean plaza;
    
    @JoinColumns({
        @JoinColumn(name = "id_cap", referencedColumnName = "id_cap"),
        @JoinColumn(name = "id_dep", referencedColumnName = "id_dep")})
    @ManyToOne(optional = false)
    private EscCap escCap;

    @JoinColumn(name = "id_esc", referencedColumnName = "id_esc")
    @ManyToOne(optional = false)
    private Employee employee;
    
}
